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

public class Comparison {
	public enum Data{
		ND(0),NP(1),NX(2),PD(3),PP(4),PX(5);
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
	public Comparison(String URL, String Path, String Index, String State) throws IOException
	{
		DataBox = new ArrayList< ArrayList<String> >();
		path = Path;
		index = Index;
		state = State;
		File file = new File(path);
		if(!file .exists())
			file.mkdirs();
		file = new File(path + "\\" + index + "_大小对比.txt");
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
				file = new File(path + "\\" + index + "_大小对比.txt");
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
		   		if(count == 50)
		   			break;
		   		ArrayList<String> subbox = new ArrayList<String>();
		   		subbox.add(str.select("td").get(2).text().length() >= 4? str.select("td").get(2).text().substring(0,4):"0.0");
		   		String pan = str.select("td").get(3).text();
	   			if(!Common_Use.is_num(pan) && !pan.equals(""))
	   				pan = pan.substring(0, pan.length()-1);
		   		subbox.add(pan);
		   		subbox.add(str.select("td").get(4).text().length() >= 4? str.select("td").get(4).text().substring(0,4):"0.0");
		   		subbox.add(str.select("td").get(6).text().length() >= 4? str.select("td").get(6).text().substring(0,4):"0.0");
		   		String ppan = str.select("td").get(7).text();
		   		subbox.add(ppan);
		   		subbox.add(str.select("td").get(8).text().length() >= 4? str.select("td").get(8).text().substring(0,4):"0.0");
		   		this.DataBox.add(subbox);
		   		count++;
		   	}
		   	return;
	    }catch (IOException e) {
			// TODO Auto-generated catch block
	    	e.printStackTrace();
	    	Lists.textarea.append("大小对比连接超时！！\n");
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
	
	
	//计算符合盘口大小条件公司数
	//Instruction: XY </=/<= XY  X-n为即时，p为初始
	//                           Y-d大球，x小球，p盘口
	public double Get_Data(String Code, int t)
	{
		if(Common_Use.is_num(Code))
			return Double.parseDouble(Code);
		switch(Code)
		{  
		    case "nd": return Common_Use.parsedata(this.DataBox.get(t).get(Data.ND.GetPos()), 0);
		    case "nx": return Common_Use.parsedata(this.DataBox.get(t).get(Data.NX.GetPos()), 0);
		    case "np":  String pan = this.DataBox.get(t).get(Data.NP.GetPos());
			   			if(pan.indexOf('/') != -1)
			   				return Double.parseDouble(pan.substring(0,pan.indexOf('/')))+0.25;
			   			else if(Common_Use.is_num(pan))
			   				return Double.parseDouble(pan);
		    case "pd": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PD.GetPos()), 0);
		    case "px": return Common_Use.parsedata(this.DataBox.get(t).get(Data.PX.GetPos()), 0);
		    case "pp":  String ppan = this.DataBox.get(t).get(Data.PP.GetPos());
			   			if(ppan.indexOf('/') != -1)
			   				return Double.parseDouble(ppan.substring(0,ppan.indexOf('/')))+0.25;
			   			else if(Common_Use.is_num(ppan))
			   				return Double.parseDouble(ppan);
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
	
	public double[] daxiao()
	{
	   	double r1 = 0, r2 = 0, r3 = 0, r4 = 0;
	   	int count = -1;
	   	double daqiu,xiaoqiu;
   		double pdaqiu,pxiaoqiu;
   		String pan;
   		String ppan;
   		Double dpan=10.0,dppan=10.0;
	   	for (int t = 0 ; t < this.DataBox.size() ; t++)
	   	{
	   		if(count == 50)
	   			break;
	   		pdaqiu  = Common_Use.parsedata(this.DataBox.get(t).get(Data.PD.GetPos()), 0);
	   		daqiu   = Common_Use.parsedata(this.DataBox.get(t).get(Data.ND.GetPos()), 0);
	   		pxiaoqiu= Common_Use.parsedata(this.DataBox.get(t).get(Data.PX.GetPos()), 0);
		   	xiaoqiu = Common_Use.parsedata(this.DataBox.get(t).get(Data.NX.GetPos()), 0);
			pan = this.DataBox.get(t).get(Data.NP.GetPos());
   			ppan = this.DataBox.get(t).get(Data.PP.GetPos());
   			if(ppan.indexOf('/') != -1)
   				dppan = Double.parseDouble(ppan.substring(0,ppan.indexOf('/')))+0.25;
   			else if(Common_Use.is_num(ppan))
   				dppan = Double.parseDouble(ppan);
   			if(pan.indexOf('/') != -1)
   				dpan = Double.parseDouble(pan.substring(0,pan.indexOf('/')))+0.25;
   			else if(Common_Use.is_num(pan))
   				dpan = Double.parseDouble(pan);
			//大小初盘-即盘不小于1,初始大不小于1.16,即时大不大于0.65——项目14/DD
	   		if((dppan - dpan >= 1 || pdaqiu >= 1.16 || daqiu <= 0.65) && count < 30)
	   			r1++;
	   		//1到20的公司有1家以上的公司即时大小盘减去初始大小盘大于等于1——项目16/DD2
	   		if(dpan - dppan >= 1 && count < 20)
	   			r2++;
	   		//大小对比1到12的公司中即时盘－初盘＞0.5的公司数＞4家——项目64/QDSP
	   		if(dpan - dppan > 0.5 && count < 12)
	   			r3++;
	   		//大小对比中1到10的公司，即时盘－初盘＜﹣0.5的公司总数＞3家——项目65/QDSP2
	   		if(dpan - dppan < -0.5 && count < 10)
	   			r4++;
   			count++;
	   	}
	   	//[0]DD,[1]即盘-初盘>=1,[2]即时盘－初盘＞0.5,[3]即时盘－初盘＜﹣0.5
	   	double[] result = {r1,r2,r3,r4};
	   	return result;
	}
}
