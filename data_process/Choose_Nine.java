package data_process;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import display.Lists;

public class Choose_Nine {
	public static class nine
	{
		public double win;
		public double draw;
		public double lose;
		public String zhu;
		public String ke;
		public String pei;
		public String comp;
		public String time;
		nine(String c,String t,String z,String k, double w,double d,double l,String p)
		{
			comp = c;
			time = t;
			zhu = z;
			ke = k;
			pei = p;
			win = w;draw = d;lose = l;
		}
	}
	public static nine[] msg = new nine[14];
	public static String[][] rxj()
	{
		String[][] res = new String[14][2];
		for(int i = 0 ; i < 14 ; i++)
		{
			res[i][0] = "";
			res[i][1] = "";
		}
		double min = 1000;
		int serial = 0;
		try {
			Document doc = Jsoup.connect("http://trade.500.com/rcjc/").timeout(5000).get();
			Element masthead = doc.getElementById("vsTable");
			Elements links = masthead.select("tr");
		   	int count = -7;
		   	boolean not_set = true;
		   	int spf = 0;
		   	for (Element str : links) 
		   	{
		   		if(count < 0)
		   		{
		   			count++;
		   			continue;
		   		}
		   		String comp = str.select("td").get(1).text();
		   		String time = str.select("td").get(2).text();
		   		String zhu = str.select("td").get(3).select("span").get(1).text();
		   		String ke  = str.select("td").get(3).select("span").get(3).text();
		   		//System.out.println(zhu+"  "+ke);
		   		double win  = Double.parseDouble(str.select("td").get(5).select("span").get(0).text());
		   		double draw = Double.parseDouble(str.select("td").get(5).select("span").get(1).text());
		   		double lose = Double.parseDouble(str.select("td").get(5).select("span").get(2).text());
		   		System.out.println(win);
		   		if(win < min)
		   		{
		   			min = win;
		   			spf = 0;
		   			serial = count;
		   		}
		   		else if(draw < min)
		   		{
		   			min = draw;
		   			spf = 1;
		   			serial = count;
		   		}
		   		else if(lose < min)
		   		{
		   			min = lose;
		   			spf = 2;
		   			serial = count;
		   		}
		   		msg[count] = new nine(comp,time,zhu,ke,win,draw,lose,Lists.sort(win,draw,lose));
		   		//System.out.println(sort(win,draw,lose));
	   			count++;
		   	}
		   	//任九方案1
		   	int[] two = {0,1,1,1,0,1,1,1,0};
		   	int[] one = {1,0,0,0,1,0,0,0,1};
		   	int A=0 , B=0 , C=0;
		   	while(not_set)
		   	{
		   		int[] choose = new int[14];
		   		int[][] select = new int[14][3];
		   		choose[serial] = 1;
		   		int three1,three2,two1,two2,two3,one1,one2,one3,one4;
		   		int s;
		   		three1 = choose_num(choose);
		   		choose[three1] = 1;
		   		select = set_select(select,three1,1,1,1);
		   		three2 = choose_num(choose);
		   		choose[three2] = 1;
		   		select = set_select(select,three2,1,1,1);
		   		two1 = choose_num(choose);
		   		choose[two1] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,two1,two[s*3],two[s*3+1],two[s*3+2]);
		   		two2 = choose_num(choose);
		   		choose[two2] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,two2,two[s*3],two[s*3+1],two[s*3+2]);
		   		two3 = choose_num(choose);
		   		choose[two3] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,two3,two[s*3],two[s*3+1],two[s*3+2]);
		   		one1 = choose_num(choose);
		   		choose[one1] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,one1,one[s*3],one[s*3+1],one[s*3+2]);
		   		one2 = choose_num(choose);
		   		choose[one2] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,one2,one[s*3],one[s*3+1],one[s*3+2]);
		   		one3 = choose_num(choose);
		   		choose[one3] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,one3,one[s*3],one[s*3+1],one[s*3+2]);
		   		one4 = choose_num(choose);
		   		choose[one4] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,one4,one[s*3],one[s*3+1],one[s*3+2]);
		   		for(int i = 0 ; i < 14 ; i++)
		   			for(int j = 0 ; j < 3 ; j++)
		   			{
		   				if(select[i][j] == 1)
		   				{
		   					if(msg[i].pei.charAt(j) == 'A')
		   						A++;
		   					else if(msg[i].pei.charAt(j) == 'B')
		   						B++;
		   					else if(msg[i].pei.charAt(j) == 'C')
		   						C++;
		   				}
		   			}
		   		if(A > 6 && B > 5 && C > 3)
		   		{
		   			not_set = false;
		   			for(int i = 0 ; i < 14 ; i++)
		   			{
		   				//System.out.println(select[i][0] + "   " + select[i][1] + "   " +select[i][2]);
		   				if(select[i][0] == 0 && select[i][1] == 0 && select[i][2] == 0)
		   					res[i][0] = "*";
		   				if(select[i][0] == 1)
		   					res[i][0] += "3";
		   				if(select[i][1] == 1)
		   					res[i][0] += "1";
		   				if(select[i][2] == 1)
		   					res[i][0] += "0";
		   			}
		   			/*String print = "";
		   			for(int i = 0 ; i < 14 ; i++)
		   			{
		   				print += res[i][0];
		   				if(i < 13)
		   					print += ",";
		   			}
		   			System.out.println(print);*/
		   		}
		   	}
		  //任九方案2
		   	not_set = true;
		   	A=0;B=0;C=0;
		   	while(not_set)
		   	{
		   		int[] choose = new int[14];
		   		int[][] select = new int[14][3];
		   		choose[serial] = 1;
		   		select[serial][spf] = 1;
		   		int three1,three2,two1,two2,two3,one1,one2,one3;
		   		int s;
		   		three1 = choose_num(choose);
		   		choose[three1] = 1;
		   		select = set_select(select,three1,1,1,1);
		   		three2 = choose_num(choose);
		   		choose[three2] = 1;
		   		select = set_select(select,three2,1,1,1);
		   		two1 = choose_num(choose);
		   		choose[two1] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,two1,two[s*3],two[s*3+1],two[s*3+2]);
		   		two2 = choose_num(choose);
		   		choose[two2] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,two2,two[s*3],two[s*3+1],two[s*3+2]);
		   		two3 = choose_num(choose);
		   		choose[two3] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,two3,two[s*3],two[s*3+1],two[s*3+2]);
		   		one1 = choose_num(choose);
		   		choose[one1] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,one1,one[s*3],one[s*3+1],one[s*3+2]);
		   		one2 = choose_num(choose);
		   		choose[one2] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,one2,one[s*3],one[s*3+1],one[s*3+2]);
		   		one3 = choose_num(choose);
		   		choose[one3] = 1;
		   		s = (int) Math.round(Math.random()*2);
		   		select = set_select(select,one3,one[s*3],one[s*3+1],one[s*3+2]);
		   		for(int i = 0 ; i < 14 ; i++)
		   			for(int j = 0 ; j < 3 ; j++)
		   			{
		   				if(select[i][j] == 1)
		   				{
		   					if(msg[i].pei.charAt(j) == 'A')
		   						A++;
		   					else if(msg[i].pei.charAt(j) == 'B')
		   						B++;
		   					else if(msg[i].pei.charAt(j) == 'C')
		   						C++;
		   				}
		   			}
		   		if(A > 6 && B > 5 && C > 3)
		   		{
		   			not_set = false;
		   			for(int i = 0 ; i < 14 ; i++)
		   			{
		   				//System.out.println(select[i][0] + "   " + select[i][1] + "   " +select[i][2]);
		   				if(select[i][0] == 0 && select[i][1] == 0 && select[i][2] == 0)
		   					res[i][1] = "*";
		   				if(select[i][0] == 1)
		   					res[i][1] += "3";
		   				if(select[i][1] == 1)
		   					res[i][1] += "1";
		   				if(select[i][2] == 1)
		   					res[i][1] += "0";
		   			}
		   			/*String print = "";
		   			for(int i = 0 ; i < 14 ; i++)
		   			{
		   				print += res[i][1];
		   				if(i < 13)
		   					print += ",";
		   			}
		   			System.out.println(print);*/
		   		}
		   	}
		   	return res;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("任选9场连接超时！！");
			System.out.println("----------------------");
			if(Lists.wrong_time >= 8)
			{
				Lists.textarea.append("网络连接错误！\n");
				Lists.start_over = true;
				while(!Lists.change){}
				Lists.wrong_time = 0;
				String[][] result = new String[14][2];
				return result;
			}
			Lists.wrong_time++;
			//textarea.append("大小对比连接超时！\n重新连接\n");
			//textarea.setCaretPosition(textarea.getText().length());
			return rxj();
		}
	}
	public static int[][] set_select(int[][] sel,int ser,int s,int p ,int f)
	{
		sel[ser][0] = s;
		sel[ser][1] = p;
		sel[ser][2] = f;
		return sel;
	}
	public static int choose_num(int[] ch)
	{
		int t;
   		do
   			t = (int) Math.round(Math.random()*13);
   		while(ch[t] == 1);
   		return t;
	}
}
