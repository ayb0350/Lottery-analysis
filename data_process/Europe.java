package data_process;
import display.Lists;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Europe {
	public enum Data{
		NAME(0),PW(1),NW(2),PD(3),ND(4),PL(5),NL(6),BACK(7);
		private int pos;
		private Data(int p)
		{
			pos = p;
		}
		public int GetPos()
		{
			return pos;
		}
	}
	public static String url;
	public String path;
	public String index;
	public String state;
	public static Document doc;
	public Element masthead;
	public Elements Now_link;
	public Elements Init_link;
	public ArrayList< ArrayList<String> > DataBox;
	public Europe(String URL, String Path, String Index, String State) throws IOException
	{
		DataBox = new ArrayList< ArrayList<String> >();
		path = Path;
		index = Index;
		state = State;
		File file = new File(path);
		if(!file .exists())
			file.mkdirs();
		file = new File(path + "\\" + index + "_百家欧赔.txt");
		if(file.isFile() & file.exists() & (state.equals("完") | state.equals("改期")))
		{
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null)
            {
            	String[] str = lineTxt.split("#");
            	ArrayList<String> subbox = new ArrayList<String>();
            	for(String s : str)
            		subbox.add(s);
            	this.DataBox.add(subbox);
            }
            read.close();
		}
		else
		{
			UpDateData(URL);
			if(state.equals("完") | state.equals("改期"))
			{
				file = new File(path);
				if(!file .exists())
					file.mkdirs();
				file = new File(path + "\\" + index + "_百家欧赔.txt");
				if(!file.exists())
			    	file.createNewFile();
				FileOutputStream fileWritter = new FileOutputStream(file);
			    BufferedWriter bufferWritter = new BufferedWriter(new OutputStreamWriter( 
			    		fileWritter, "UTF-8"));
			    for(ArrayList<String> array : DataBox)
			    {
			    	for(String Strs : array)
			    		bufferWritter.write(Strs + "#");
			    	bufferWritter.write("\r\n");
			    }
			    bufferWritter.close();
			}
		}
	}
	public void UpDateData(String URL)
	{
		try{
			url = URL;
			doc = Jsoup.connect(url).timeout(10000).get();
			masthead = doc.getElementById("datatb");
			if(masthead == null)
				return;
			Init_link = masthead.getElementsByAttributeValue("ttl", "sx1");
			Now_link = masthead.getElementsByAttributeValue("ttl", "sx2");
			int count = 0;
			for (Element str_init:this.Init_link)
		   	{
		   		Element str_now = this.Now_link.get(count);
		   		if(str_init.attr("class").equals("tr3")||str_now.attr("class").equals("tr3"))
		   			continue;
		   		if(count == 30)
		   			break;
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		subbox.add(str_init.select("td").get(1).text());
		   		subbox.add(str_init.select("td").get(2).text());
		   		subbox.add(str_now.select("td").get(0).text());
		   		subbox.add(str_init.select("td").get(3).text());
		   		subbox.add(str_now.select("td").get(1).text());
		   		subbox.add(str_init.select("td").get(4).text());
		   		subbox.add(str_now.select("td").get(2).text());
		   		String st = str_now.select("td").get(6).text();
		   		subbox.add(st.substring(0,st.length()-1));
		   		this.DataBox.add(subbox);
		   		count++;
		   	}
			return;
	    }catch (IOException e) {
	    	e.printStackTrace();
	    	Lists.textarea.append("百家欧赔连接超时！！\n");
			if(Lists.wrong_time >= 8)
			{
				Lists.textarea.append("网络连接错误！\n");
				Lists.start_over = true;
				while(!Lists.change){}
				Lists.wrong_time = 0;
			}
			Lists.wrong_time++;
			UpDateData(URL);
	    }
	}
	//获取指定公司胜平负赔率
	//Company_Code：293-威廉希尔  8-SNAI 12-Oddset (奥德赛特) 2-Ladbrokes (立博) 3-Bet365 Interwetten
	//Select_Code:XY  X-p为初始陪率，n为即时赔率      Y-w胜赔，d平赔，l负陪 
	public double Company_state(String Company,String Select_Code)
	{
		for(int t = 0 ; t < this.DataBox.size() ; t++)
		{
			String Name = this.DataBox.get(t).get(Data.NAME.GetPos());
			//if(Name.equals("立博")) Name = "Ladbrokes (立博)";
			//if(Name.equals("Bet365")) Name = "bet365";
			if(Company.equals(Name))
				switch(Select_Code)
				{
					case "nw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NW.GetPos()), 0);
					case "nd": return Common_Use.parsedata(this.DataBox.get(t).get(Data.ND.GetPos()), 0);
					case "nl": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NL.GetPos()), 0);
					case "pw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PW.GetPos()), 0);
					case "pd": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PD.GetPos()), 0);
					case "pl": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PL.GetPos()), 0);
					default : return 0;
				}
		}
		return -10;
	}
	
	//计算符合胜平负条件公司数
	//Instruction: XY </=/<= XY  X-n为即时赔率，p为初始赔率
	//                           Y-w胜赔，d平赔，l负陪
	public double Get_Odds(String Code, int t)
	{
		if(Common_Use.is_num(Code))
			return Double.parseDouble(Code);
		switch(Code)
		{  
		    case "nw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NW.GetPos()), 0);
		    case "pw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PW.GetPos()), 0);
		    case "nd": return Common_Use.parsedata(this.DataBox.get(t).get(Data.ND.GetPos()), 0);
		    case "pd": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PD.GetPos()), 0);
		    case "nl": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NL.GetPos()), 0);
		    case "pl": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PL.GetPos()), 0);
		    default: return 0;
		}
	}
	public int Match_Condition(int Range, String Instruction)
	{
		String And_Or;
		if(Instruction.indexOf("&") != -1)
			And_Or = "&";
		else if(Instruction.indexOf(" or ") != -1)
			And_Or = " or ";
		else
			And_Or = "null";
		String Ins[] = Instruction.split(And_Or);
		int count = 0,result = 0;
		for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
	   		if(count == Range)
	   			break;
	   		boolean res = And_Or.equals(" or ")? false:true;
	   		for(String str : Ins)
			{
	   			String s[] = new String[2];
	   			if(str.indexOf("<=") != -1){
					s = str.split("<=");
					if(Get_Odds(s[0],t) <= Get_Odds(s[1],t))
					{
						if(!And_Or.equals(" or ")) res = res && true;
						else res = res || true;
					}
					else
					{
						if(!And_Or.equals(" or ")) res = res && false;
						else res = res || false;
					}
				}
	   			else if(str.indexOf("<") != -1){
					s = str.split("<");
					if(Get_Odds(s[0],t) < Get_Odds(s[1],t))
					{
						if(!And_Or.equals(" or ")) res = res && true;
						else res = res || true;
					}
					else
					{
						if(!And_Or.equals(" or ")) res = res && false;
						else res = res || false;
					}
				}
				else if(str.indexOf("=") != -1){
					s = str.split("=");
					if(Get_Odds(s[0],t) == Get_Odds(s[1],t))
					{
						if(!And_Or.equals(" or ")) res = res && true;
						else res = res || true;
					}
					else
					{
						if(!And_Or.equals(" or ")) res = res && false;
						else res = res || false;
					}
				}
			}
	   		if(res)
	   			result++;
	   		count++;
	   	}
		return result;
	}
	
	//计算平均赔率
	//Instruction: XY </=/<= XY  X-a为平均赔率
	//                           Y-w胜赔，d平赔，l负陪
	public double[] Average_Odds(int Range, String Ins)
	{
		double Wsum = 0, Dsum = 0, Lsum = 0;
		double Wave = 0, Dave = 0, Lave = 0;
		int count = 0;
		for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
	   		if(count == Range)
	   			break;
	   		if(Ins.equals("n"))
	   		{
		   		Wsum += Common_Use.parsedata(this.DataBox.get(t).get(Data.NW.GetPos()), 0);
		   		Dsum += Common_Use.parsedata(this.DataBox.get(t).get(Data.ND.GetPos()), 0);
		   		Lsum += Common_Use.parsedata(this.DataBox.get(t).get(Data.NL.GetPos()), 0);
		   	}
	   		else if(Ins.equals("p"))
	   		{
		   		Wsum += Common_Use.parsedata(this.DataBox.get(t).get(Data.PW.GetPos()), 0);
		   		Dsum += Common_Use.parsedata(this.DataBox.get(t).get(Data.PD.GetPos()), 0);
		   		Lsum += Common_Use.parsedata(this.DataBox.get(t).get(Data.PL.GetPos()), 0);
		   	}
	   		count++;
	   	}
		Wave = Wsum/Range;
		Dave = Dsum/Range;
		Lave = Lsum/Range;
		double result[] = {Wave,Dave,Lave};
		return result;
	}
	
	//求最大最小赔率
	//Ins nw胜赔  nd平赔  nl负赔
	public double[] Max_Min(int Range, String Ins)
	{
		double maxodds = 0, minodds = 1000;
		int count = 0;
		for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
			double odds;
	   		if(count == Range)
	   			break;
	   		odds = Get_Odds(Ins,t);
	   		if(odds > maxodds)
	   			maxodds = odds;
	   		if(odds < minodds)
	   			minodds = odds;
	   		count++;
	   	}
		double res[] = {maxodds,minodds};
		return res;
	}
	
	public double[] ouzhi() 
	{
	   	double r1 = 0, r2 = 0, r3 = 0, r4 = 1, r5 = 0, r6 = 1, r7 = 0, r8 = 0,
	   		   r9 = 0, r10 = 0, r11 = 0, r12 = 0, r13 = 0, r14 = 0, r15 = 0,
	   		   r16 = 0, r17 = 0;
	   	int count = 0;
	   	double maxf = 0, minf = 1000;
   		double maxs = 0, mins = 1000;
   		double maxf1 = 0, minf1 = 1000;
   		double maxf2 = 0, minf2 = 1000;
	   	for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
	   		double now_win  = Common_Use.parsedata(this.DataBox.get(t).get(Data.NW.GetPos()), 0);
	   		double pre_win  = Common_Use.parsedata(this.DataBox.get(t).get(Data.PW.GetPos()), 0);
	   		double now_draw = Common_Use.parsedata(this.DataBox.get(t).get(Data.ND.GetPos()), 0);
	   		double pre_draw = Common_Use.parsedata(this.DataBox.get(t).get(Data.PD.GetPos()), 0);
	   		double now_lose = Common_Use.parsedata(this.DataBox.get(t).get(Data.NL.GetPos()), 0);
	   		double pre_lose = Common_Use.parsedata(this.DataBox.get(t).get(Data.PL.GetPos()), 0);
	   		double fanh = Common_Use.parsedata(this.DataBox.get(t).get(Data.BACK.GetPos()), 0);
	   		//1到30的公司中有20家以上即时赔率满足：胜赔率大于2.00，同时平赔率减去胜赔率大于0.90——项目21/F
	   		if(now_win > 2.00 && now_draw - now_win > 0.9 && count < 30)
	   			r1++;
	   		//序号1到20的公司中返还率值在92％到94％的公司负赔率的最大值和最小值之间的差距小于0.6——项目23/PB
	   		if(count < 20)
	   		{
	   			if(fanh >= 92.00 && fanh <= 94.00)
	   			{
	   				maxf = now_lose > maxf? now_lose:maxf;
	   				minf = now_lose < minf? now_lose:minf;
	   				maxs = now_win > maxs? now_win:maxs;
	   				mins = now_win < mins? now_win:mins;
	   			}
	   			if(fanh >= 91.00 && fanh <= 94.00)
	   			{
	   				maxf1 = now_lose > maxf1? now_lose:maxf1;
	   				minf1 = now_lose < minf1? now_lose:minf1;
	   			}
	   			if(fanh >= 91.00 && fanh <= 95.00)
	   			{
	   				maxf2 = now_lose > maxf2? now_lose:maxf2;
	   				minf2 = now_lose < minf2? now_lose:minf2;
	   			}
	   		}
	   		if(count >= 20){
	   			r2 = Math.abs(maxf - minf);
	   			if(r2==1000) r2 = -1;
	   			r10 = Math.abs(maxs - mins);
	   			if(r10==1000) r10 = -1;
	   			r11 = Math.abs(maxf1 - minf1);
	   			if(r11==1000) r11 = -1;
	   			r12 = Math.abs(maxf2 - minf2);
	   			if(r12==1000) r12 = -1;
	   		}
	   		//即时欧赔中序号1到20的公司中出现胜平赔率相同的公司数≥1家——项目25
	   		//相同项（胜平的赔率）都＜3.26——项目35
	   		if(now_win == now_draw && count < 20)
	   		{
	   			r3++;
	   			if(now_win >= 3.26)
	   				r4 = 0;
	   		}
	   		if(count >= 20 && r3 == 0)
	   			r4 = 0;
	   		//即时欧赔中序号1到20的公司中出现平负赔率相同的公司数≥1家——项目26
	    	//相同项（平负的赔率）都＜3.26——项目34
	   		if(now_lose == now_draw && count < 20)
	   		{
	   			r5++;
	   			if(now_lose >= 3.26)
	   				r6 = 0;
	   		}
	   		if(count >= 20 && r5 == 0)
	   			r6 = 0;
	   		//平赔率—胜赔率≥1.00的公司数大于13家——项目27/P2
	   		if(now_draw - now_win >= 1.00 && count < 20)
	   			r7++;
	   		//平赔率—负赔率≥0.40的公司大于等于6家——项目27/P2
	   		if(now_draw - now_lose >= 0.40 && count < 20)
	   			r8++;
	   		//有2家以上（包括2家）即时赔率平赔与胜负两个赔率中的最大的赔率相差≥0.5——项目60/PJ6
	   		if(count < 30)
	   		{
	   			double odds = now_win >= now_lose? now_win:now_lose;
	   			if(Math.abs(odds - now_draw) >= 0.5)
	   				r9++;
	   		}
	   		//欧赔1到10的公司中即时平和负的赔率值相差≤0.10的公司数＞4家——项目121/FK4
	   		if(Math.abs(now_draw - now_lose) <= 0.10 & count < 10)
	   			r13++;
	   	    //欧赔1到10的公司中即时胜和平的赔率值相差≤0.10的公司数＞4家——项目122/FZ
	   		if(Math.abs(now_draw - now_win) <= 0.10 & count < 10)
	   			r14++;
	   		//欧赔1到10的公司负的即时赔率－初始赔率≥1.00的公司≥4家
	   		if(now_lose - pre_lose >= 1.00 & count < 10)
	   			r15++ ;
	   		//欧赔1到10的公司负的即时赔率－初始赔率≥1.00的公司≥4家
	   		if(pre_win - now_win > 0.30 & count < 10)
	   			r16++ ;
	   		//欧赔1到10的公司负的即时赔率－初始赔率≥1.00的公司≥4家
	   		if(now_lose - pre_lose > 1.00 & count < 10)
	   			r17++ ;
	   		count++;
	   	}
	   	//[0]F-c,[1]返还率92%-94%最大最小负赔差   [2]平和胜的赔率相同   [3]相同<3.26
	   	//[4]负和平的赔率相同       [5]相同<3.26  [6]平赔—胜赔≥1.00   [7]平赔率—负赔率≥0.40
	   	//[8]平赔与胜负赔中大的相差≥0.5  [9]返还率92%-94%最大最小胜赔差
	   	//[10]返还率91%-94%最大最小负赔差     [11]返还率91%-95%最大最小负赔差
	   	//[12]即时平和负的赔率值相差≤0.10  [13]即时胜和平的赔率值相差≤0.10
	   	//[14]负的即时赔率－初始赔率≥1.00  [15]胜的初始赔率－即时赔率>0.30
	   	//[16]负的即时赔率－初始赔率>1.00
	   	double[] result = {r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17};
	   	return result;
	}
}
