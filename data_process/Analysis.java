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

public class Analysis {
	public enum Data{
		ZG(0),KG(1),SF(2),PK(3),DX(4),ZK(5);
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
	public Document doc;
	public String state;
	public static Element masthead1; //交战史
	public Elements links1;
	public static Element masthead2; //主队主场
	public Elements links2;
	public static Element masthead3; //客队客场
	public Elements links3;
	public static Element masthead4; //主队近期
	public Elements links4;
	public static Element masthead5; //客队近期
	public Elements links5;
	public String Id;
	public String Hash;
	public ArrayList< ArrayList<String> > DataBox_jf;
	public ArrayList< ArrayList<String> > DataBox_z;
	public ArrayList< ArrayList<String> > DataBox_k;
	public ArrayList< ArrayList<String> > DataBox_zz;
	public ArrayList< ArrayList<String> > DataBox_kk;
	public int rankz, rankk, total;
	public int[][] Num = new int[4][3];
	public Analysis(String URL, String Path, String Index, String State) throws IOException
	{
		DataBox_jf = new ArrayList< ArrayList<String> >();
		DataBox_z = new ArrayList< ArrayList<String> >();
		DataBox_k = new ArrayList< ArrayList<String> >();
		DataBox_zz = new ArrayList< ArrayList<String> >();
		DataBox_kk = new ArrayList< ArrayList<String> >();
		path = Path;
		index = Index;
		state = State;
		File file = new File(path);
		if(!file .exists())
			file.mkdirs();
		file = new File(path + "\\" + index + "_数据分析.txt");
		if(file.isFile() & file.exists() & (state.equals("完") | state.equals("改期")))
		{
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            lineTxt = bufferedReader.readLine();
            String[] st = lineTxt.split("#");
            rankz = Integer.parseInt(st[0]);
            rankk = Integer.parseInt(st[1]);
            total = Integer.parseInt(st[2]);
            for(int i = 0; i < 4; i++)
            {
                lineTxt = bufferedReader.readLine();
                st = lineTxt.split("#");
                for(int j = 0; j < 3; j++)
                	Num[i][j] = Integer.parseInt(st[j]);
            }
            int flag = 0;
            while((lineTxt = bufferedReader.readLine()) != null)
            {
            	switch(lineTxt)
            	{
            	case "<两队交锋": flag = 1; continue; 
            	case "<主队主场": flag = 2; continue;
            	case "<客队客场": flag = 3; continue;
            	case "<主队最近": flag = 4; continue;
            	case "<客队最近": flag = 5; continue;
            	}
            	String[] str = lineTxt.split("#");
            	ArrayList<String> subbox = new ArrayList<String>();
            	for(String s : str)
            		subbox.add(s);
            	switch(flag)
            	{
            	case 1: this.DataBox_jf.add(subbox);break;
            	case 2: this.DataBox_zz.add(subbox);break;
            	case 3: this.DataBox_kk.add(subbox);break;
            	case 4: this.DataBox_z.add(subbox);break;
            	case 5: this.DataBox_k.add(subbox);break;
            	}
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
				file = new File(path + "\\" + index + "_数据分析.txt");
				if(!file.exists())
			    	file.createNewFile();
				FileOutputStream fileWritter = new FileOutputStream(file);
			    BufferedWriter bufferWritter = new BufferedWriter(new OutputStreamWriter( 
			    		fileWritter, "UTF-8"));
			    bufferWritter.write(rankz + "#" + rankk + "#" + total + "\r\n");
			    for(int i = 0; i < 4; i++)
			    	bufferWritter.write(Num[i][0] + "#" + Num[i][1] + "#" + Num[i][2] + "\r\n");
			    bufferWritter.write("<两队交锋" + "\r\n");
			    for(ArrayList<String> array : DataBox_jf)
			    {
			    	for(String Strs : array)
			    		bufferWritter.write(Strs + "#");
			    	bufferWritter.write("\r\n");
			    }
			    bufferWritter.write("<主队主场" + "\r\n");
			    for(ArrayList<String> array : DataBox_zz)
			    {
			    	for(String Strs : array)
			    		bufferWritter.write(Strs + "#");
			    	bufferWritter.write("\r\n");
			    }
			    bufferWritter.write("<客队客场" + "\r\n");
			    for(ArrayList<String> array : DataBox_kk)
			    {
			    	for(String Strs : array)
			    		bufferWritter.write(Strs + "#");
			    	bufferWritter.write("\r\n");
			    }
			    bufferWritter.write("<主队最近" + "\r\n");
			    for(ArrayList<String> array : DataBox_z)
			    {
			    	for(String Strs : array)
			    		bufferWritter.write(Strs + "#");
			    	bufferWritter.write("\r\n");
			    }
			    bufferWritter.write("<客队最近" + "\r\n");
			    for(ArrayList<String> array : DataBox_k)
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
			Id = doc.getElementsByAttributeValue("id", "id").attr("value");
			Hash = doc.getElementsByAttributeValue("id", "hash").attr("value");
			masthead1 = doc.getElementById("team_jiaozhan");
			links1 = masthead1 != null? masthead1.select("tr"):null;
			masthead2 = doc.getElementById("team_zhanji2_1");
			links2 = masthead2 != null? masthead2.getElementsByClass("pub_table").select("tr"):null;
			masthead3 = doc.getElementById("team_zhanji2_0");
			links3 = masthead3 != null? masthead3.getElementsByClass("pub_table").select("tr"):null;
			masthead4 = doc.getElementById("team_zhanji_1");
			links4 = masthead4 != null? masthead4.getElementsByClass("pub_table").select("tr"):null;
			masthead5 = doc.getElementById("team_zhanji_0");
			links5 = masthead5 != null? masthead5.getElementsByClass("pub_table").select("tr"):null;
			if(this.doc.getElementsByClass("bd").size() == 0) total = 0;
			else
			{
				Elements Msg = this.doc.getElementsByClass("tb-c").select("tbody").select("tr");
				total = Msg.size();
			}
			int Temp;
			Elements Msgs = this.doc.getElementsByClass("integral");
			Element Msg = null;
			for(Element temp : Msgs)
			{
				if(temp.select("h2").get(0).text().equals("赛前联赛积分排名"))
				{
					Msg = temp;
					break;
				}
			}
			for(int k = 0; k < 2; k++)
			{
				String str_Temp;
				if(Msg == null)
					Temp = 0;
				else
				{
					str_Temp = Msg.select("tbody").get(k).select("tr").get(1).select("td").get(9).text();
					Temp = str_Temp.equals("")? -1:	Integer.parseInt(str_Temp);
				}
				if(k == 0)
					rankz = Temp;
				else
					rankk = Temp;
				for(int i = 2; i < 5; i++)
				{
					if(Msg == null)
						Temp = -1;
					else
					{
						str_Temp = Msg.select("tbody").get(k).select("tr").get(2).select("td").get(i).text();
						Temp = str_Temp.equals("")? -1:	Integer.parseInt(str_Temp);
					}
					Num[k * 2][i - 2] = Temp;
				}
				for(int i = 2; i < 5; i++)
				{
					if(Msg == null)
						Temp = -1;
					else
					{
						str_Temp = Msg.select("tbody").get(k).select("tr").get(3).select("td").get(i).text();
						Temp = str_Temp.equals("")? -1:	Integer.parseInt(str_Temp);
					}
					Num[k * 2 + 1][i - 2] = Temp;
				}
			}
			int count = -1;
			for (Element str : this.links1)
		   	{
		   		if(count == -1)
		   		{
		   			count++;
		   			continue;
		   		}
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		String bf = str.select("a").get(1).text();
		   		String zhu = str.getElementsByClass("zhu").text();
		   		bf = bf.replaceAll(":"," ");
		   		String[] strarray = bf.split(" ");
		   		strarray[1] = strarray[1].substring(1);
		   		strarray[2] = strarray[2].substring(0,strarray[2].length()-1);
		   		String zgoal,kgoal;
		   		if(strarray[0].equals(zhu))
		   		{
		   			zgoal = strarray[1];
		   			kgoal = strarray[2];
		   		}
		   		else
		   		{
		   			zgoal = strarray[2];
		   			kgoal = strarray[1];
		   		}
		   		subbox.add(zgoal);
		   		subbox.add(kgoal);
		   		subbox.add(str.select("td").get(6).text());
		   		subbox.add(str.select("td").get(7).text());
		   		subbox.add(str.select("td").get(9).text());
		   		if(strarray[0].equals(zhu))
		   			subbox.add("主");
		   		else
		   			subbox.add("客");
		   		this.DataBox_jf.add(subbox);
		   		count++;
		   	}
			count = -1;
		   	for (Element str : this.links2)
		   	{
		   		if(count == -1)
		   		{
		   			count++;
		   			continue;
		   		}
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		String bf = str.select("a").get(1).text();
		   		bf = bf.replaceAll(":"," ");
		   		String[] strarray = bf.split(" ");
		   		strarray[1] = strarray[1].substring(1);
		   		strarray[2] = strarray[2].substring(0,strarray[2].length()-1);
		   		subbox.add(strarray[1]);
		   		subbox.add(strarray[2]);
		   		subbox.add(str.select("td").get(4).text());
		   		subbox.add(str.select("td").get(3).text());
		   		subbox.add(str.select("td").get(6).text());
		   		subbox.add("主");
		   		this.DataBox_zz.add(subbox);
		   		count++;
		   	}
		   	count = -1;
		   	for (Element str : this.links3) 
		   	{
		   		if(count == -1)
		   		{
		   			count++;
		   			continue;
		   		}
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		String bf = str.select("a").get(1).text();
		   		bf = bf.replaceAll(":"," ");
		   		String[] strarray = bf.split(" ");
		   		strarray[1] = strarray[1].substring(1);
		   		strarray[2] = strarray[2].substring(0,strarray[2].length()-1);
		   		subbox.add(strarray[1]);
		   		subbox.add(strarray[2]);
		   		subbox.add(str.select("td").get(4).text());
		   		subbox.add(str.select("td").get(3).text());
		   		subbox.add(str.select("td").get(6).text());
		   		subbox.add("客");
		   		this.DataBox_kk.add(subbox);
		   		count++;
		   	}
			count = -1;
		   	for (Element str : this.links4) 
		   	{
		   		if(count == -1)
		   		{
		   			count++;
		   			continue;
		   		}
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		String bf = str.select("a").get(1).text();
		   		String zhu = str.getElementsByClass("zhu").text();
		   		bf = bf.replaceAll(":"," ");
		   		String[] strarray = bf.split(" ");
		   		strarray[1] = strarray[1].substring(1);
		   		strarray[2] = strarray[2].substring(0,strarray[2].length()-1);
		   		String zgoal,kgoal;
		   		if(strarray[0].equals(zhu))
		   		{
		   			zgoal = strarray[1];
		   			kgoal = strarray[2];
		   		}
		   		else
		   		{
		   			zgoal = strarray[2];
		   			kgoal = strarray[1];
		   		}
		   		subbox.add(zgoal);
		   		subbox.add(kgoal);
		   		subbox.add(str.select("td").get(4).text());
		   		subbox.add(str.select("td").get(3).text());
		   		subbox.add(str.select("td").get(6).text());
		   		if(zhu.equals(strarray[0]))
		   			subbox.add("主");
		   		else
		   			subbox.add("客");
		   		this.DataBox_z.add(subbox);
		   		count++;
		   	}
		   	count = -1;
		   	for (Element str : this.links5) 
		   	{
		   		if(count == -1)
		   		{
		   			count++;
		   			continue;
		   		}
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		String bf = str.select("a").get(1).text();
		   		String zhu = str.getElementsByClass("zhu").text();
		   		bf = bf.replaceAll(":"," ");
		   		String[] strarray = bf.split(" ");
		   		strarray[1] = strarray[1].substring(1);
		   		strarray[2] = strarray[2].substring(0,strarray[2].length()-1);
		   		String zgoal,kgoal;
		   		if(strarray[0].equals(zhu))
		   		{
		   			zgoal = strarray[1];
		   			kgoal = strarray[2];
		   		}
		   		else
		   		{
		   			zgoal = strarray[2];
		   			kgoal = strarray[1];
		   		}
		   		subbox.add(zgoal);
		   		subbox.add(kgoal);
		   		subbox.add(str.select("td").get(4).text());
		   		subbox.add(str.select("td").get(3).text());
		   		subbox.add(str.select("td").get(6).text());
		   		if(zhu.equals(strarray[0]))
		   			subbox.add("主");
		   		else
		   			subbox.add("客");
		   		this.DataBox_k.add(subbox);
		   		count++;
		   	}
	    }catch (IOException e) {
			// TODO Auto-generated catch block
	    	e.printStackTrace();
	    	Lists.textarea.append("数据分析连接超时！！\n");
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
	
	public ArrayList< ArrayList<String> > Get_Links(int Code)
	{
		switch(Code)
		{
		case 1: return this.DataBox_jf;
		case 2: return this.DataBox_zz;
		case 3: return this.DataBox_kk;
		case 4: return this.DataBox_z;
		case 5: return this.DataBox_k;
		default: return null;
		}
	}
	
	//求最大最小数据
	//Ins:X-p为即时盘口
	public double Get_Data(String Code, int t)
	{
		if(Common_Use.is_num(Code))
			return Double.parseDouble(Code);
		switch(Code)
		{  
		case "p1": return Common_Use.transform(this.DataBox_jf.get(t).get(Data.PK.GetPos()));
		case "p2": return Common_Use.parsedata(this.DataBox_zz.get(t).get(Data.PK.GetPos()), 10);
		case "p3": return Common_Use.parsedata(this.DataBox_kk.get(t).get(Data.PK.GetPos()), 10);
		case "p4": return Common_Use.parsedata(this.DataBox_z.get(t).get(Data.PK.GetPos()), 10);
		case "p5": return Common_Use.parsedata(this.DataBox_k.get(t).get(Data.PK.GetPos()), 10);
		case "zg1": return Common_Use.parsedata(this.DataBox_jf.get(t).get(Data.ZG.GetPos()), 0);
		case "zg2": return Common_Use.parsedata(this.DataBox_zz.get(t).get(Data.ZG.GetPos()), 0);
		case "zg3": return Common_Use.parsedata(this.DataBox_kk.get(t).get(Data.ZG.GetPos()), 0);
		case "zg4": return Common_Use.parsedata(this.DataBox_z.get(t).get(Data.ZG.GetPos()), 0);
		case "zg5": return Common_Use.parsedata(this.DataBox_k.get(t).get(Data.ZG.GetPos()), 0);
		case "kg1": return Common_Use.parsedata(this.DataBox_jf.get(t).get(Data.KG.GetPos()), 0);
		case "kg2": return Common_Use.parsedata(this.DataBox_zz.get(t).get(Data.KG.GetPos()), 0);
		case "kg3": return Common_Use.parsedata(this.DataBox_kk.get(t).get(Data.KG.GetPos()), 0);
		case "kg4": return Common_Use.parsedata(this.DataBox_z.get(t).get(Data.KG.GetPos()), 0);
		case "kg5": return Common_Use.parsedata(this.DataBox_k.get(t).get(Data.KG.GetPos()), 0);
		default:    return 0;
		}
	}
	public double[] Max_Min(/*int Range,*/ String Ins)
	{
		double maxpan = -10, minpan = 10;
		int index = Integer.parseInt(""+Ins.charAt(1));
		ArrayList< ArrayList<String> > DataBox = Get_Links(index);
		for (int t = 0 ; t < DataBox.size() ; t++)
	   	{
			double pan = 0;
	   		pan = Get_Data(Ins,t);
	   		if(pan > maxpan & pan!=10.0)
	   			maxpan = pan;
	   		if(pan < minpan)
	   			minpan = pan;
	   	}
		double res[] = {maxpan,minpan};
		return res;
	}
	
	//进球失球盘口符合条件次数
	public int GP_Condition(int Range, String Instruction)
	{
		String And_Or;
		if(Instruction.indexOf("&") != -1)
			And_Or = "&";
		else if(Instruction.indexOf(" or ") != -1)
			And_Or = " or ";
		else
			And_Or = "null";
		String Ins[] = Instruction.split(And_Or);
   		int index;
   		int result = 0;
   		String st[] = new String[2];
   		if(Instruction.indexOf("<=") != -1)
   			st = Ins[0].split("<=");
   		if(Instruction.indexOf("<") != -1)
   			st = Ins[0].split("<");
   		else if(Instruction.indexOf("=") != -1)
   			st = Ins[0].split("=");
   		if(Common_Use.is_num(st[0]))
   			index = Integer.parseInt(""+st[1].charAt(st[1].length()-1));
   		else
   			index = Integer.parseInt(""+st[0].charAt(st[0].length()-1));
   		ArrayList< ArrayList<String> > DataBox = Get_Links(index);
   		for(int t = 0 ; t < DataBox.size() ; t++)
		{
	   		if(t == Range)
	   			break;
	   		boolean res = And_Or.equals(" or ")? false:true;
	   		for(String ss:Ins)
	   		{
	   			String s[] = new String[2];
	   			if(ss.indexOf("<=") != -1){
					s = ss.split("<=");
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
	   			else if(ss.indexOf("<") != -1){
					s = ss.split("<");
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
				else if(ss.indexOf("=") != -1){
					s = ss.split("=");
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
		}
		return result;
	}
	
	//胜平负大小次数
	//Ins:XY X-s/p/f胜平负  d/x大小 
	//       Y-1近期交战  2-主队主场  3-客队客场  4-主队近期  5-客队近期
	public String Get_SF(String Code, int t)
	{
		ArrayList< ArrayList<String> > DataBox = Get_Links(Integer.parseInt(Code.charAt(1)+""));
		if(t >= DataBox.size())
			return "-";
		if(Code.charAt(0) == 's' || Code.charAt(0) == 'p' || Code.charAt(0) == 'f')
			return DataBox.get(t).get(Data.SF.GetPos());
		else if(Code.charAt(0) == 'd' || Code.charAt(0) == 'x')
			return DataBox.get(t).get(Data.DX.GetPos());
		return "";
	}
	public int Match_Condition(int Range, String Ins)
	{
		int index = Integer.parseInt(""+Ins.charAt(1));
		int result = 0;
		ArrayList< ArrayList<String> > DataBox = Get_Links(index);
		String s = ""+Ins.charAt(0);
		for (int t = 0 ; t < DataBox.size() ; t++)
	   	{
	   		if(t == Range)
	   			break;
	   		switch(s)
	   		{
	   		case "s": if(Get_SF(Ins,t).equals("胜")) result++; break;
	   		case "p": if(Get_SF(Ins,t).equals("平")) result++; break;
	   		case "f": if(Get_SF(Ins,t).equals("负")) result++; break;
	   		case "d": if(Get_SF(Ins,t).equals("大")) result++; break;
	   		case "x": if(Get_SF(Ins,t).equals("小")) result++; break;
	   		}
	   	}
		return result;
	}
	
	//连续大小
	public int Series(int Range, String Ins)
	{
		int index = Integer.parseInt(""+Ins.charAt(1));
		int result = 0;
		boolean flag = true;
		ArrayList< ArrayList<String> > DataBox = Get_Links(index);
		for (int t = 0 ; t < DataBox.size() ; t++)
	   	{
	   		if(t == Range)
	   			break;
	   		String s = Ins.charAt(0)+"";
	   		String st;
	   		switch(s)
	   		{
	   		case "s": st = "胜";break;
	   		case "p": st = "平";break;
	   		case "f": st = "负";break;
	   		case "d": st = "大";break;
	   		case "x": st = "小";break;
	   		default: st = "null";
	   		}
	   		if(Get_SF(Ins,t).equals(st) && flag)
	   			result++;
	   		else if(!Get_SF(Ins,t).equals(st))
	   			flag = false;
	   	}
		return result;
	}
	
	public int Get_Rank(String Code)
	{
		if(Code.equals("z"))
			return rankz;
		else if(Code.equals("k"))
			return rankk;
		else if(Code.equals("zd"))
			return total == 0 | rankz == 0? -1 : total - rankz + 1;
		else
			return total == 0 | rankk == 0? -1 : total - rankk + 1;
	}
	
	public int Get_List(String Code)
	{
		int index1 = 0, index2;
		if(Code.charAt(0) == 'k') index1 += 2;
		if(Code.charAt(1) == 'k') index1 += 1;
		if(Code.charAt(2) == 's') index2 = 0;
		else if(Code.charAt(2) == 'p') index2 = 1;
		else index2 = 2;
		return Num[index1][index2];
	}
	
	public double[] fenxi()
	{
		//两队近期交战盘口最大、最小值——项目2
		//**********************************************************************
	   	int flag = 0;
	   	double r1 = 0,r2 = 10,r3 = 0,r7 = 0;
	   	//double aomen = 10;
	   	for (int t = 0 ; t < this.DataBox_jf.size() ; t++)
	   	{
	   		//if(t == 0)
	   			//aomen = Common_Use.transform(this.DataBox_jf.get(t).get(Data.PK.GetPos()));
	   		//String dx = this.DataBox_jf.get(t).get(Data.DX.GetPos());
	   		String sf = this.DataBox_jf.get(t).get(Data.SF.GetPos());
	   		String pk = this.DataBox_jf.get(t).get(Data.PK.GetPos());
	   		int zgoal = Integer.parseInt(this.DataBox_jf.get(t).get(Data.ZG.GetPos()));
	   		int kgoal = Integer.parseInt(this.DataBox_jf.get(t).get(Data.KG.GetPos()));
	   		String zk = this.DataBox_jf.get(t).get(Data.ZK.GetPos());
	   	    //两队交锋＞2次——项目S1
	   		r1++;
	   	    //亚盘对比页面中序号1到12的公司中2家以上的公司的即时盘口和在交战历史中的最近一次主场比赛的澳门终盘相同——项目10/XP
	   		if(zk.equals("主") && flag == 0)
	   		{
	   			flag = 1;
	   			r2 = Common_Use.transform(pk);
	   		}	
	   		if(t == 0)
	   		{
	   			//最近一次交锋主队主场胜——项目10/XP
	   			if(zk.equals("客"))
	   				r3 = 4;
	   		    else if(sf.equals("胜"))
	   		    	r3 = 3;
	   			else if(sf.equals("平"))
	   				r3 = 1;
	   			else if(sf.equals("负"))
	   				r3 = 0;
	   		}
	   		//主队不为主场全胜
	   		if(r7 == 0 & zk.equals("主") & zgoal <= kgoal)
	   			r7 = 1;
	   	}
	  //主队近10个主场盘口最大、最小值——项目3
	  //**********************************************************************
	   	double r4 = 0, r6 = 0;
	   	boolean sw_flag = true;
	   	for (int t = 0 ; t < this.DataBox_zz.size() ; t++)
	   	{
	   		String zan = this.DataBox_zz.get(t).get(Data.SF.GetPos());
	   		//String zdx = this.DataBox_zz.get(t).get(Data.DX.GetPos());
	   		int zgoal = (int) Common_Use.parsedata(this.DataBox_zz.get(t).get(Data.ZG.GetPos()), -1);
   			int kgoal = (int) Common_Use.parsedata(this.DataBox_zz.get(t).get(Data.KG.GetPos()), -1);
	   		if(sw_flag & zan.equals("胜"))
	   			r4++;
	   		if(r4 > 0 & !zan.equals("胜"))
	   			sw_flag = false;
	   		if(zgoal-kgoal <= 1 & zgoal != -1 & kgoal != -1 & t < 5)
	   			r6++;
	   	}
	   	//客队近10个客场盘口最大、最小值
	  //**********************************************************************
	   	double r5 = 0;
	   	boolean sl_flag = true;
	   	for (int t = 0 ; t < this.DataBox_kk.size() ; t++) 
	   	{
	   		String zan = this.DataBox_kk.get(t).get(Data.SF.GetPos());
	   		//String kdx = this.DataBox_kk.get(t).get(Data.DX.GetPos());
	   		if(sl_flag & zan.equals("负"))
	   			r5++;
	   		if(r5 > 0 & !zan.equals("负"))
	   			sl_flag = false;
	   	}
	    //主队近5场平负场数
	   	//**********************************************************************
	   	for (int t = 0 ; t < this.DataBox_z.size() ; t++) 
	   	{
	   		//String zan = this.DataBox_z.get(t).get(Data.SF.GetPos());
	   		//String zdx = this.DataBox_z.get(t).get(Data.DX.GetPos());
	   	}
	   	//客队近10场比赛
	  //**********************************************************************
	   	for (int t = 0 ; t < this.DataBox_k.size() ; t++) 
	   	{
	   		//String zan = this.DataBox_k.get(t).get(Data.SF.GetPos());
	   		//String kdx = this.DataBox_k.get(t).get(Data.DX.GetPos());
	   	}
	   	//[0]交锋次数           [1]交战最近一次主场盘         [2]最近交战主队在主场胜
	   	//[3]主队主场最近连胜     [4]客队客场最近连胜      [5]主队近5主场赢球<=1
	   	//[6]主队不为主场全胜
	   	double[] result = {r1,r2,r3,r4,r5,r6,r7};
	   	return result;
	}
	
	public static String pick(String str,int zk)
	{
		String st;
		int flag = 1;
		int begin=0,end=0;
		for(int i=0 ; i<str.length() ; i++)
		{
			st = ""+str.charAt(i);
			if(st.equals(":"))
				flag = 0;
			if(st.equals(" ") && flag == 1 && zk == 1)
			{
				begin = 0;
				end = 0;
			}
			if(st.equals(" ") && flag == 0 && zk == 0)
				break;
			if(Common_Use.is_num(st) && flag == zk)
			{
				if(begin==0)
				{
					begin = i;
					end = i+1;
				}
				else
					end = i+1;
			}
		}
		return str.substring(begin,end);
	}
	
	
	public static class CompPair<A,B,C>
	{
		public A first;
		public B second;
		public C third;
		public C forth;
		public CompPair(A a,B b,C c)
		{
			first = a;
			second = b;
			third = c;
		}
		public CompPair(A a,B b,C c,C d)
		{
			first = a;
			second = b;
			third = c;
			forth = d;
		}
	}
}
