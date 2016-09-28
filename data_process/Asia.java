package data_process;

import display.Lists;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Asia {
	public enum Data{
		NAME(0),NLW(1),NP(2),NRW(3),PLW(4),PP(5),PRW(6);
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
	public Elements links;
	public ArrayList< ArrayList<String> > DataBox;
	public Asia(String URL, String Path, String Index, String State) throws IOException
	{
		DataBox = new ArrayList< ArrayList<String> >();
		path = Path;
		index = Index;
		state = State;
		File file = new File(path);
		if(!file .exists())
			file.mkdirs();
		file = new File(path + "\\" + index + "_亚盘对比.txt");
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
				file = new File(path + "\\" + index + "_亚盘对比.txt");
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
			links = masthead.select("tr");
			int count = -1;
		   	for (Element str : this.links)
		   	{
		   		if(count==-1)
		   		{
		   			count++;
		   			continue;
		   		}
		   		if(str.attr("class").equals("tr3"))
		   			continue;
		   		if(count == 30)
		   			break;
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		subbox.add(str.select("td").get(1).text().equals("")? " ":str.select("td").get(1).text());
		   		subbox.add(str.select("td").get(2).text().length() >= 5? str.select("td").get(2).text().substring(0,5):"0");
		   		String pankou  = str.select("td").get(3).attr("ref");
		   		subbox.add(pankou);
		   		subbox.add(str.select("td").get(4).text().equals("")? " ":str.select("td").get(4).text());
		   		subbox.add(str.select("td").get(6).text().length() >= 5? str.select("td").get(6).text().substring(0,5):"0");
		   		String pankou0 = str.select("td").get(7).attr("ref");
		   		subbox.add(pankou0);
		   		subbox.add(str.select("td").get(8).text().equals("")? " ":str.select("td").get(8).text());
		   		this.DataBox.add(subbox);
		   		count++;
		   	}
		   	return;
	    }catch (IOException e) {
	    	e.printStackTrace();
	    	Lists.textarea.append("亚盘对比连接超时！！\n");
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
	
	//获取指定公司数据
	//Company_Code：澳门 Interwetten
	//Select_Code:XY  X-p为初始，n为赔率      Y-p盘口，l左水，w右水
	public double Company_state(String Company,String Select_Code)
	{
		for(int t = 0 ; t < this.DataBox.size() ; t++)
		{
			String Name = this.DataBox.get(t).get(Data.NAME.GetPos());
			if(Company.equals(Name))
				switch(Select_Code)
				{
				case "np" : return Common_Use.parsedata(this.DataBox.get(t).get(Data.NP.GetPos()), -10);
				case "nlw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NLW.GetPos()), 0);
				case "nrw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NRW.GetPos()), 0);
				case "pp" : return Common_Use.parsedata(this.DataBox.get(t).get(Data.PP.GetPos()), -10);
				case "plw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PLW.GetPos()), 0);
				case "prw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PRW.GetPos()), 0);
				default : return 0;
				}
		}
		return -10;
	}
	
	//计算符合盘口水位条件公司数
	//Instruction: XY </=/<= XY  X-n为即时，p为初始
	//                           Y-lw左水，rw右水，p盘口
	public double Get_Data(String Code, int t)
	{
		if(Common_Use.is_num(Code))
			return Double.parseDouble(Code);
		switch(Code)
		{  
		    case "nlw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NLW.GetPos()), 0);
		    case "nrw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NRW.GetPos()), 0);
		    case "np":  return Common_Use.parsedata(this.DataBox.get(t).get(Data.NP.GetPos()), -10);
		    case "plw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PLW.GetPos()), 0);
		    case "prw": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PRW.GetPos()), 0);
		    case "pp":  return Common_Use.parsedata(this.DataBox.get(t).get(Data.PP.GetPos()), -10);
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
	   			if (str.indexOf("<=") != -1){
					s = str.split("<=");
					if(Get_Data(s[0],t) <= Get_Data(s[1],t))
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
					if(Get_Data(s[0],t) < Get_Data(s[1],t))
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
					if(Get_Data(s[0],t) == Get_Data(s[1],t))
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
	
	//求最大最小盘口
	//Ins:X-np为即时盘口  pp为初始盘口
	public double[] Max_Min(int Range, String Ins)
	{
		double maxpan = -10, minpan = 10;
		int count = 0;
		for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
			double pan = 0;
	   		if(count == Range)
	   			break;
	   		pan = Get_Data(Ins,t);
	   		if(pan > maxpan)
	   			maxpan = pan;
	   		if(pan < minpan)
	   			minpan = pan;
	   		count++;
	   	}
		double res[] = {maxpan,minpan};
		return res;
	}
	
	//盘口相同数量
	public int Sum_Pan(int Range, double p, boolean abs)
	{
		int count = 0;
		int res = 0;
		for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
			double pan = 0;
	   		if(count == Range)
	   			break;
	   		pan = Get_Data("np",t);
	   		if(abs && Math.abs(pan) == Math.abs(p))
   				res++;
	   		else if(!abs && pan == p)
	   			res++;
	   		count++;
	   	}
		return res;
	}
	
	//盘口相同水位条件
	public int Pan_Water(int Range, double p, double w)
	{
		int count = 0;
		int result = 0;
		for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
			double pan = 0;
	   		if(count == Range)
	   			break;
	   		pan = Get_Data("np",t);
	   		double lw = Get_Data("nlw",t);
	   		if(pan == p)
	   			if(lw > w)
	   				result++;
	   		count++;
	   	}
		return result;
	}
	public double[] yazhi()
	{
		double r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0,r6 = 0, r7 = 0, r8 = 0, r9 = 0, r10 = 0, r11 = 0;
	   	ArrayList<pak> array = new ArrayList<pak>();
	   	ArrayList<pak> array1 = new ArrayList<pak>();
	   	int count = -1;
	   	for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
	   		double water_left,water_right,water_rightp,water_leftp;
	   		water_left  = Common_Use.parsedata(this.DataBox.get(t).get(Data.NLW.GetPos()), 0);
		   	water_right = Common_Use.parsedata(this.DataBox.get(t).get(Data.NRW.GetPos()), 0);
		   	water_rightp= Common_Use.parsedata(this.DataBox.get(t).get(Data.PRW.GetPos()), 0);
		   	water_leftp = Common_Use.parsedata(this.DataBox.get(t).get(Data.PLW.GetPos()), 0);
		   	String pankou = this.DataBox.get(t).get(Data.NP.GetPos());
		   	String pankou0 = this.DataBox.get(t).get(Data.PP.GetPos());
	   		double pk  = Common_Use.parsedata(pankou, -10);
	   		double pk0 = Common_Use.parsedata(pankou0, -10);
	   		
	   		//在亚盘对比页面中的即时盘口（左边的盘口）在序号1到10的公司中出现同样的盘口的公司有1家以上（包括一家）左边的水≥1.040——项目28
	   		//同时,出现同样盘口的公司有1家以上（包括1家）右边的水≥1.040——项目28/P3
	   		int biao = 0;
	   		if(count < 10)
	   		{
	   			for(pak x : array)
	   				if(pankou.equals(x.pan))
	   				{
	   					biao = 1;
	   					if(water_left >= 1.040)
	   						x.left++;
	   					if(water_right >= 1.040)
	   						x.right++;
	   					x.time++;
	   					break;
	   				}
	   			if(biao == 0)
	   			{
	   				int l=0,r=0;
	   				if(water_left >= 1.040) l++;
   					if(water_right >= 1.040) r++;
	   				pak mid = new pak(pankou,l,r,1);
	   				array.add(mid);
	   			}
	   		}
	   		if(count >= 10)
	   			for(pak x : array)
	   				if(x.time > 1 && x.left >1 && x.right > 1)
	   					r1 = 1;
	   		//在亚盘对比的序号1到10的公司中,即时盘中相同的盘口,左边的水相差超过0.20——项目44
	   		biao = 0;
	   		if(count < 10)
	   		{
	   			for(pak x : array1)
	   				if(pankou.equals(x.pan))
	   				{
	   					biao = 1;
	   					if(water_left > x.max_water)
	   						x.max_water = water_left;
	   					if(water_left < x.min_water)
	   						x.min_water = water_left;
	   				}
	   			if(biao == 0)
	   			{
	   				pak mid = new pak(pankou,water_left,water_left);
	   				array1.add(mid);
	   			}
	   		}
	   		if(count >= 10)
	   			for(pak x : array1)
	   				if(x.max_water - x.min_water > 0.20)
	   					r2 = 1;
	   		//亚盘对比页面中序号1到15的赔率公司中4家以上的公司即时盘口-初始盘口=0.25，
	   		//同时即时盘口和初始盘口左边的水都≥1.000——项目49/SP3
	   		if(pk - pk0 == 0.25 & pk != -10 & pk0 != -10 & water_left >= 1.000 & water_leftp >= 1.000 & count < 15)
	   			r3++;
	   		//亚盘对比1到12的公司中即时盘的盘口－初始盘口＜－0.5的公司数＞4家——项目64/QDSP
	   		if(pk - pk0 < -0.5 & pk != -10 & pk0 != -10 & count < 12)
	   			r4++;
	   		//亚盘1到10的公司中终盘盘口减去初盘盘口＞0.25＞5家——项目65/QDSP2
	   		if(pk - pk0 > 0.25 & pk != -10 & pk0 != -10 & count < 10)
	   			r5++;
	   		//亚盘1到10的公司初盘减去即时盘≥0.25的公司数＞5家——项目85/PF7
	   		if(pk0 - pk >= 0.25 & pk != -10 & pk0 != -10 & count < 10)
	   			r6++;
	   	    //终盘减去初盘≥0.25
	   		if(pk - pk0 >= 0.25 & pk != -10 & pk0 != -10 & count < 10)
	   			r7++;
	   		//1到10的公司中即时盘口减去初始盘口等于0.25，并且即时盘口和初始盘口左边的水都小于1.000
	   		if(pk - pk0 == -0.25 & pk != -10 & pk0 != -10 & water_left < 1 & water_leftp < 1 & count < 10)
	   			r8++;
	   		//1到10的公司初始盘口减去即时盘口大于等于0.25
	   		if(pk0 - pk >= 0.25 & pk != -10 & pk0 != -10 & count < 10)
	   			r9++;
	   	    //1到10的公司中初始盘口减去即时盘口等于0.25
	   		if(pk0 - pk == 0.25 & pk != -10 & pk0 != -10 & count < 10)
	   			r10++;
	   	    //1到10的公司中初始盘口减去即时盘口等于-0.25
	   		if(pk0 - pk == -0.25 & pk != -10 & pk0 != -10 & count < 10)
	   			r11++;
	   		count++;
	   	}
	   	//[0]P3,[1]O,[2]即时盘口-初始盘口=0.25,两水>=1,[3]即时盘口-初始盘口<-0.5
	   	//[4]即时盘－初盘≥0.25左水＞1.005 //[5]初盘-即时盘≥0.25 //[6]终盘-初盘≥0.25
	   	//[7]即时盘口减去初始盘口等于0.25，并且即时盘口和初始盘口左边的水都小于1.000
	   	//[8]1到10的公司初始盘口减去即时盘口大于等于0.25
	   	//[9]1到10的公司初始盘口减去即时盘口等于0.25
	   	//[10]1到10的公司初始盘口减去即时盘口等于-0.25
	   	double[] result = {r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11};
	   	return result;
	}
	
	public static class pak
	{
		public String pan;
		public int left;
		public int right;
		public double max_water;
		public double min_water;
		public int time;
		pak(String str,int l,int r, int t)
		{
			pan = str;
			left = l;
			right = r;
			time = t;
		}
		pak(String str,double max,double min)
		{
			pan = str;
			max_water = max;
			min_water = min;
			left = 0;
			right = 0;
		}
	}

}
