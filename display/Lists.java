package display;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import data_process.Analysis;
import data_process.Analysis.CompPair;
import data_process.Asia;
import data_process.Choose_Nine;
import data_process.Common_Use;
import data_process.Comparison;
import data_process.DoSomething;
import data_process.Europe;
import data_process.InfoStore;
import display.EvenOddRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Lists implements ItemListener,ActionListener
{
	public static DefaultTableModel tableModel;
	public static DefaultTableModel tableModel1;
	public static JTable table;
	public static JTable rxj;
	public static JTable spf;
	public static boolean start_over = false; 
	public static boolean sta_over = false;
	public static boolean change = false;
	public static String leistr = "";
	public static int leixing = 3; 
	public static int wrong_time = 0;
	public static ArrayList<CompPair<String,String,Double>> ArrayRed = 
			new ArrayList<CompPair<String,String,Double>>();
	public static TextArea textarea = new TextArea("足彩赔率分析系统\n",10,20);
	public static TextArea datatext = new TextArea("",2,20,TextArea.SCROLLBARS_NONE);
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints parameter = new GridBagConstraints();
	JPanel panel = new JPanel(gridbag);
	GridBagLayout gridbag1 = new GridBagLayout();    //设置为网格袋布局
	GridBagConstraints parameter1 = new GridBagConstraints();
	JPanel panel1 = new JPanel(gridbag1);
	JPanel panel2 = new JPanel();
	GridBagLayout gridbag3 = new GridBagLayout();
	GridBagConstraints parameter3 = new GridBagConstraints();
	JPanel panel3 = new JPanel(gridbag3);
	GridBagLayout gridbag4 = new GridBagLayout();
	GridBagConstraints parameter4 = new GridBagConstraints();
	JPanel panel4 = new JPanel(gridbag4);
	
	Choice choice;
	public static Choice choice0;
	public static Choice choice1;
	JLabel label = new JLabel("至"); 
	Calendar cal;
	ButtonGroup group = new ButtonGroup();
	JRadioButton RB1 = new JRadioButton ("完整版",false);  //单选按钮
	JRadioButton RB2 = new JRadioButton ("单场比分",false);
	JRadioButton RB3 = new JRadioButton ("竞彩比分",true);  //单选按钮
	JRadioButton RB4 = new JRadioButton ("足彩比分",false);
	JRadioButton RB5 = new JRadioButton ("完场比分",false);  //单选按钮
	JRadioButton RB6 = new JRadioButton ("未来赛事",false);
	Object[][] playerInfo;
	String[] Names;
	ArrayList<InfoStore> Q = new ArrayList<InfoStore>();
	Buttonclicked buttonclicked = new Buttonclicked();
	protected JButton Button1 = new JButton("重 新 加 载");
	protected JButton Button2 = new JButton("条 件 解 释");
	protected JButton Button3 = new JButton("退  出");
	protected JButton Button4 = new JButton("任 猜 九 场");
	protected JButton Button5 = new JButton("数 据 统 计");
	protected JButton Button6 = new JButton("开 始 统 计");
	Statistics statistics;
	DoSomething ds;
	Thread t;
	String[] PanK = {"四球", "三球半/四球", "三球半", "三球/三球半", "三球", "两球半/三球", "两球半", "两球/两球半", "两球", "球半/两球", "球半", "一球/球半", "一球", "半球/一球", "半球", "平手/半球", "平手",
			"受平手/半球", "受半球", "受半球/一球", "受一球", "受一球/球半", "受球半", "受球半/两球", "受两球", "受两球/两球半", "受两球半", "受两球半/三球", "受三球", "受三球/三球半", "受三球半", "受三球半/四球", "受四球"};
	public Lists() throws IOException
	{		
		//Class.forName("com.hxtt.sql.access.AccessDriver");
		//con = DriverManager.getConnection("jdbc:Access:///./Soccer.accdb","","");
		JFrame f=new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    playerInfo= new Object[][] {};
	    tableModel = new DefaultTableModel(playerInfo,Titles.Head)
	    {
	    	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column)
			{
	            return false;
	        }//表格不允许被编辑
	    };
	    table=new JTable(tableModel);
	    table.setDefaultRenderer(Object.class,new EvenOddRenderer()); 
	    table.getTableHeader().setReorderingAllowed(false);
	    table.setPreferredScrollableViewportSize(new Dimension(1100,900));
	    CompPair<String,String,Double> P;
	    String str;
		URL url = Lists.class.getResource("/display/Red_Code");  
		InputStream in = url.openStream(); 
		InputStreamReader read = new InputStreamReader(in, "GBK");
        BufferedReader br = new BufferedReader(read);
		for(int i = 9 ; (str = br.readLine()) != null && str != null ; i++)
		{
			//System.out.println(str);
			if(str.indexOf(",") == -1)
			{
				i--;
				//System.out.println("********************************");
				continue;
			}
			String s[] = str.split(",");
			if(s.length == 2)
				P = new CompPair<String,String,Double>(Titles.Head[i],s[0],Double.parseDouble(s[1]));
			else
				P = new CompPair<String,String,Double>(Titles.Head[i],s[0],Double.parseDouble(s[1]),Double.parseDouble(s[2]));
			//System.out.println(Titles.Head[i] + "  " + str);
			ArrayRed.add(P);
		}
		br.close();
    	for(int i = 0 ; i < Titles.Head.length ; i++)
	    {
    		if(i == 1)
    		{
    			table.getColumnModel().getColumn(i).setMinWidth(250);
    	    	table.getColumnModel().getColumn(i).setMaxWidth(500);
    		}
    		else if(i < 9)
    		{
    			table.getColumnModel().getColumn(i).setMinWidth(80);
    	    	table.getColumnModel().getColumn(i).setMaxWidth(500);
    		}
    		else
    		{
    			table.getColumnModel().getColumn(i).setMinWidth(0);
    	    	table.getColumnModel().getColumn(i).setMaxWidth(0);
    		}
	    }
	    table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
	    //***************************************************************************
		textarea.setEditable(false);
		textarea.setFont(new Font("宋体",Font.PLAIN,14));
	    parameter.anchor=GridBagConstraints.CENTER;
		parameter.fill = GridBagConstraints.BOTH;
		parameter.gridwidth = GridBagConstraints.REMAINDER;
		parameter.insets=new Insets(10,50,0,50);
		gridbag.setConstraints(textarea, parameter);
		panel.add(textarea);
		//***************************************************************************
		choice0 = new Choice();
	    choice1 = new Choice();
	    ArrayList<String> Index = SetDC("",36);
	    for(String s : Index)
	    	choice0.add(s);
	    Index = SetDC(choice0.getSelectedItem(),36);
	    for(String s : Index)
	    	choice1.add(s);
	    choice1.select(Index.get(Index.size()-1));
	    datatext.setEditable(false);
	    datatext.setFont(new Font("黑体",Font.BOLD,16)); 
		panel4.setBorder(BorderFactory.createTitledBorder("数据统计"));
		parameter4.anchor=GridBagConstraints.CENTER;
		parameter4.fill = GridBagConstraints.NONE;
		parameter4.gridwidth = GridBagConstraints.REMAINDER;
		gridbag4.setConstraints(datatext, parameter4);
		panel4.add(datatext);
		datatext.setText("统计停止！");
		parameter4.gridwidth = GridBagConstraints.BOTH;
		parameter4.insets=new Insets(10,0,10,0);
		gridbag4.setConstraints(choice0, parameter4);
		panel4.add(choice0);
		choice0.setEnabled(true);
		gridbag4.setConstraints(label, parameter4);
		panel4.add(label);
		parameter4.gridwidth = GridBagConstraints.REMAINDER;
		gridbag4.setConstraints(choice1, parameter4);
		panel4.add(choice1);
		choice1.setEnabled(true);
		parameter4.gridwidth = GridBagConstraints.BOTH;
		parameter4.insets=new Insets(0,0,10,0); 
		gridbag4.setConstraints(Button6, parameter4);
		panel4.add(Button6);
		parameter4.gridwidth = GridBagConstraints.REMAINDER;
		parameter4.insets=new Insets(0,10,10,0); 
		gridbag4.setConstraints(Button5, parameter4);
		panel4.add(Button5);
		Button5.setEnabled(false);
		parameter.insets=new Insets(0,50,10,50);
		gridbag.setConstraints(panel4, parameter);
		panel.add(panel4);
		//***************************************************************************
		group.add(RB1);   //打包单选按钮
		group.add(RB2);
		group.add(RB3);
		group.add(RB4);
		group.add(RB5);   
		group.add(RB6);
		panel2.setBorder(BorderFactory.createTitledBorder("类型选择")); //设置两组单选按钮面板边框
		panel2.setLayout(new GridLayout(3,2,20,0));
		panel2.add(RB1);
		panel2.add(RB2);
		panel2.add(RB3);
		panel2.add(RB4);
		panel2.add(RB5);
		panel2.add(RB6);
		parameter.insets=new Insets(0,50,10,50);
		gridbag.setConstraints(panel2, parameter);
		panel.add(panel2);
		//***************************************************************************
		cal = Calendar.getInstance();
	    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	    choice = new Choice();
	    cal.add(Calendar.DAY_OF_MONTH, 0);
    	choice.add("     "+format.format(cal.getTime())+"     ");
	    for(int i = 0 ; i < 20 ; i++)
	    {
	    	cal.add(Calendar.DAY_OF_MONTH, -1);
	    	choice.add("     "+format.format(cal.getTime())+"     ");
	    }
		panel3.setBorder(BorderFactory.createTitledBorder("日期选择"));
		parameter3.anchor=GridBagConstraints.CENTER;
		parameter3.fill = GridBagConstraints.BOTH;
		parameter3.gridwidth = GridBagConstraints.REMAINDER;
		parameter3.insets=new Insets(10,0,10,0);
		gridbag3.setConstraints(choice, parameter);
		panel3.add(choice);
		//***************************************************************************
		parameter.insets=new Insets(0,50,10,50); 
		gridbag.setConstraints(panel3, parameter);
		panel.add(panel3);
		
		panel1.setBorder(BorderFactory.createTitledBorder("条件筛选"));
	    parameter1.anchor=GridBagConstraints.WEST;

	    for(int i = 0 ; i < CheckBox.JBK.length ; i++)
	    {
	    	if(i % 2 == 0)
	    		parameter1.gridwidth = GridBagConstraints.BOTH;
	    	else
	    		parameter1.gridwidth = GridBagConstraints.REMAINDER;
		    parameter1.insets=new Insets(0,0,0,0); 
			gridbag1.setConstraints(CheckBox.JBK[i], parameter1);
			panel1.add(CheckBox.JBK[i]);
			CheckBox.JBK[i].addItemListener(this);
	    }
		parameter.insets=new Insets(0,50,0,50); 
		gridbag.setConstraints(panel1, parameter);
		panel.add(panel1);
		
		parameter.gridwidth = GridBagConstraints.BOTH;
		parameter.insets=new Insets(10,60,0,0); 
		gridbag.setConstraints(Button1, parameter);
		panel.add(Button1);
		parameter.gridwidth = GridBagConstraints.REMAINDER;
		parameter.insets=new Insets(10,20,0,60); 
		gridbag.setConstraints(Button2, parameter);
		panel.add(Button2);
		parameter.gridwidth = GridBagConstraints.BOTH;
		parameter.insets=new Insets(10,60,10,0); 
		gridbag.setConstraints(Button4, parameter);
		panel.add(Button4);
		parameter.gridwidth = GridBagConstraints.REMAINDER;
		parameter.insets=new Insets(10,20,10,60); 
		gridbag.setConstraints(Button3, parameter);
		panel.add(Button3);
	    
	    Button1.addActionListener(buttonclicked);
	    Button2.addActionListener(buttonclicked);
	    Button3.addActionListener(buttonclicked);
	    Button4.addActionListener(buttonclicked);
	    Button5.addActionListener(buttonclicked);
	    Button6.addActionListener(buttonclicked);
	    
	    RB1.addActionListener(this);
	    RB2.addActionListener(this);
	    RB3.addActionListener(this);
	    RB4.addActionListener(this);
	    RB5.addActionListener(this);
	    RB6.addActionListener(this);
	    choice.addItemListener(this);
	    choice0.addItemListener(this);
	    choice1.addItemListener(this);
	    
		JScrollPane scrollPane=new JScrollPane();
	    scrollPane.setViewportView(table);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    f.getContentPane().add(scrollPane,BorderLayout.CENTER);
	    JScrollPane js = new JScrollPane(panel);
	    js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    f.getContentPane().add(js,BorderLayout.EAST);
	    f.setTitle("足彩赔率分析");
	    f.pack();
	    f.setExtendedState(Frame.MAXIMIZED_BOTH );
	    f.setVisible(true);
		ds = new DoSomething();
	}
	public ArrayList<String> getmsg(Element str, int count, Document doc, String date) throws IOException
	{
		 if(leixing == 2){
			 for(JCheckBox J : CheckBox.JR)
				 J.setEnabled(true);
		 }
		 else{
			 for(JCheckBox J : CheckBox.JR){
				 J.setEnabled(false);J.setSelected(false);}
		 }
 		 ArrayList<String> rowdata = new ArrayList<String>();
		 String time;
 		 String state;
 		 if(leixing != 5 && leixing != 6)
 		 {
 			if(str.select("td").size() == 0)
 				System.out.println(doc);
 			time  = str.select("td").get(3).text();
 	  		state = str.select("td").get(4).text().replace(Jsoup.parse("&nbsp;").text(), "进行中");
 		 }
 		 else if(leixing == 5)
 		 {
 			time  = str.select("td").get(2).text();
 	  		state = str.select("td").get(3).text().replace(Jsoup.parse("&nbsp;").text(), "进行中");
 	  		if(state.length() > 2)
 	  		{
 	  			state = "未";
 	  			leixing = 6;
 	  		}
 		 }
 		 else
 		 {
 			time  = str.select("td").get(2).text();
 	  		state = "未";
 		 }
  		 String comp   = str.select("a").get(0).text();
  		 String team1;
  		 String score1;
  		 String score2;
  		 String team2;
  		 if(leixing != 6)
  		 {
  			team1  = str.select("a").get(1).text();
  	  		score1 = str.select("a").get(2).text();
  	  		score2 = str.select("a").get(4).text();
  	  		team2  = str.select("a").get(5).text();
  		 }
  		 else
  		 {
  			team1  = str.select("a").get(1).text();
  	  		score1 = "";
  	  		score2 = "";
  	  		team2  = str.select("a").get(2).text();
  		 }
  		 String index;
  		 if(leixing != 1 && leixing != 5 && leixing != 6)
  			 index = str.select("td").get(0).text();
  		 else
  			 index = count + "";
  		 String half;
  		 if(leixing != 5 && leixing != 6)
  		 {
  			 half = str.select("td").get(8).text();
  		 }
  		 else if (leixing == 5)
  		 {
  			 half = str.select("td").get(7).text();
  		 }
  		 else
  		 {
  			 half = "-";
  		 }
  		 String linkHrefxi;
 		 String linkHrefya;
 		 String linkHrefou;
 		 String linkHrefdx;
 		 int choose = 0;
 		 switch(leixing)
 		 {
 		 case 1:choose = 10;break;
 		 case 2:choose = 11;break;
 		 case 3:choose = 11;break;
 		 case 4:choose = 9;break;
 		 case 5:choose = 8;break;
 		 case 6:choose = 7;break;
 		 }
 		 if(leixing != 2)
		 {
			 if(str.select("td").get(choose).select("a").size() < 3)
				 return null;
		 }
		 else if(str.select("td").get(11).select("a").size() < 3)
		 {
			 if(str.select("td").get(10).select("a").size() < 3)
				 return null;
			 else
				 choose = 10;
		 }
  		 linkHrefxi = str.select("td").get(choose).select("a").get(0).attr("href");
  		 linkHrefya = str.select("td").get(choose).select("a").get(1).attr("href");
  		 linkHrefou = str.select("td").get(choose).select("a").get(2).attr("href");
  		 linkHrefdx = linkHrefou.substring(0,linkHrefou.indexOf("ouzhi"))
  				 +"daxiao"+linkHrefou.substring(linkHrefou.indexOf("ouzhi")+5,linkHrefou.length());

  		 linkHrefxi = linkHrefxi.indexOf("-show-2") == -1? 
  				 linkHrefxi.substring(0, linkHrefxi.length()-6)+"-show-2":linkHrefxi;
  		 linkHrefya = linkHrefya.indexOf("-show-2") == -1? 
  				 linkHrefya.substring(0, linkHrefya.length()-6)+"-show-2":linkHrefya;
  		 linkHrefou = linkHrefou.indexOf("-show-2") == -1? 
  				 linkHrefou.substring(0, linkHrefou.length()-6)+"-show-2":linkHrefou;
  		 linkHrefdx = linkHrefdx.indexOf("-show-2") == -1?
  				 linkHrefdx.substring(0, linkHrefdx.length()-6)+"-show-2":linkHrefdx;
  		 String Path = ".\\DataFile\\";
  		 String Date;
  		 do{
  			 Date = (String) choice.getSelectedItem();
  			 //System.out.println("Get_Data");
 		 }while(Date == null && leixing != 1);
 		 switch(leixing)
 		 {
 		 case 1 : Path+="完整版\\";break;
 		 case 2 : Path+="单场比分\\"+Date.substring(7, Date.length()-7);break;
 		 case 3 : Path+="竞彩比分\\"+Date.substring(5, Date.length()-5);break;
 		 case 4 : Path+="足彩比分\\"+Date.substring(8, Date.length()-8);break;
 		 case 5 : Path+="完场比分\\"+Date.substring(5, Date.length()-5);break;
 		 case 6 : Path+="未来赛事\\"+Date.substring(5, Date.length()-5);break;
 		 }
 		 rowdata.add(index);rowdata.add(team1);
 		 rowdata.add(score1+" - "+score2);rowdata.add(team2);rowdata.add(comp);
 		 rowdata.add(time);rowdata.add(state);rowdata.add(half);
 		 Europe Oz = new Europe(linkHrefou,Path,index,state);
 		 double O[] = Oz.ouzhi();
 		 double AveO10[] = Oz.Average_Odds(10,"n");
 		 double AveO20[] = Oz.Average_Odds(20,"n");
 		 Asia Yz = new Asia(linkHrefya,Path,index,state);
 		 double[] Y = Yz.yazhi();
 		 Analysis Fx = new Analysis(linkHrefxi,Path,index,state);
 		 double[] F = Fx.fenxi();
 		 Comparison Dx = new Comparison(linkHrefdx,Path,index,state);
 		 double[] D = Dx.daxiao();
  		 DecimalFormat df=new DecimalFormat("#.##");
  		 double w=0, d=0, l=0;
  		 String colc = "";
  		 if(leixing == 2 && !start_over && !change)
		 {
  			if(str.select("td").get(9).select("span").size() >= 3)
  			{
  				w = Common_Use.parsedata(str.select("td").get(9).select("span").get(0).text(), 0);
  				d = Common_Use.parsedata(str.select("td").get(9).select("span").get(1).text(), 0);
  				l = Common_Use.parsedata(str.select("td").get(9).select("span").get(2).text(), 0);
  			}
  			else
  			{
  				String SP = doc.select("script").get(8).data();
  				String id = str.attr("fid");
  				w = match_SP(id,SP)[0];
  				d = match_SP(id,SP)[1];
  				l = match_SP(id,SP)[2];
  			}
		 }

		 //******************************
		 InfoStore Temp = new InfoStore(comp,index,time,team1,team2,score1,score2,state);
		 Temp.zp = Fx.rankz;
		 Temp.kp = Fx.rankk;
		 Temp.WiliamD = Oz.Company_state("威廉希尔", "nd");
		 Temp.MacaoP = Yz.Company_state("澳门", "np");
		 Temp.CrownP = Yz.Company_state("皇冠", "np");
		 Temp.Bet365P = Yz.Company_state("Bet365", "np");
		 Temp.zzj = Fx.Get_SF("s4", 0);
		 Temp.winG = Oz.Match_Condition(10, "pw<nw");
		 Temp.rlw1005 = Yz.Match_Condition(10, "1.005<nlw or 1.005<nrw");
		 
  		 //项目1-ZZ
		 double g_p1a = w;
		 rowdata.add(df.format(g_p1a));
		 double g_p1b = d;
		 rowdata.add(df.format(g_p1b));
		 double g_p1c = l;
		 rowdata.add(df.format(g_p1c));
		 if((g_p1a < 2.00 && g_p1a != 0)|(g_p1b < 2.00 && g_p1b != 0)|(g_p1c < 2.00 && g_p1c != 0)&leixing == 2){
			 if(colc == "") colc = "ZZ"; else colc += "、ZZ";
		 }
		 //项目2-Z4
		 String p2a = Fx.Get_SF("f1", 0);
		 rowdata.add(p2a);
		 String p2b = Fx.Get_SF("f4", 0);
		 rowdata.add(p2b);
		 String p2c = Fx.Get_SF("s5", 0);
		 rowdata.add(p2c);
		 double g_p2d = Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(df.format(g_p2d));
		 double g_p2e = Yz.Match_Condition(10, "pp=-0.5&1.005<plw");
		 rowdata.add((int)g_p2e + "个");
		 double g_p2f = Yz.Match_Condition(10, "np=-0.25&1.005<nlw");
		 rowdata.add((int)g_p2f + "个");
		 double g_p2g = Yz.Match_Condition(10, "np=0");
		 rowdata.add((int)g_p2g + "个");
		 if(p2a.equals("负") && p2b.equals("负") && p2c.equals("胜") && g_p2d > 3.40 && g_p2e > 5 &
				 g_p2f > 5 && g_p2g > 2){
 			 if(colc == "") colc = "Z4"; else colc += "、Z4";
		 }
  		 //项目3-S1
		 double g_p3a = F[0];
		 rowdata.add((int)g_p3a + "场");
 		 double g_p3b = Fx.Match_Condition((int)F[0], "d1");
 		 rowdata.add(g_p3b == F[0] && F[0]!=0? "Yes":"No");
 		 double g_p3c = Dx.Match_Condition(10, "np<3.5&1.00<nd");
 		 rowdata.add((int)g_p3c + "个");
 		 if(g_p3a > 2 && g_p3b == F[0] && g_p3c >= 4){
 			 if(colc == "") colc = "S1"; else colc += "、S1";
 		 }
 		 //项目4-S2
 		 double g_p4a = Fx.Series(10,"d4");
 		 rowdata.add((int)g_p4a+"场");
		 double g_p4b = Fx.Series(10,"d5");
		 rowdata.add((int)g_p4b+"场");
		 double g_p4c = Dx.Match_Condition(20, "1.00<=nd");
		 rowdata.add((int)g_p4c+"个");
 		 double g_p4d = Dx.Match_Condition(10, "3.5<=np");
 		 rowdata.add((int)g_p4d+"个");
		 String p4e = Fx.Get_SF("d4", 0);
		 rowdata.add(p4e);
		 String p4f = Fx.Get_SF("d5", 0);
		 rowdata.add(p4f);
		 double g_p4g = g_p3b;
		 rowdata.add(g_p4g == F[0] && F[0] > 2? "Yes":"No");
		 if((g_p4a > 4 | g_p4b > 4) && g_p4c > 10 && g_p4d < 5 && p4e.equals("大") && p4f.equals("大") && g_p4g == F[0]){
			 if(colc == "") colc = "S2"; else colc += "、S2";
		 }
		 //项目5-Z5
		 double g_p5a = F[0];
		 rowdata.add((int)g_p5a + "场");
		 String p5b = Fx.Match_Condition((int)F[0], "s1") == 0 && F[0] > 3? "Yes" : "No";
		 rowdata.add(p5b);
		 String p5c = Fx.Get_SF("s5", 0);
		 rowdata.add(p5c);
		 double g_p5d = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 rowdata.add((int)g_p5d + "场");
		 double g_p5e = Yz.Match_Condition(10, "pp=-0.25&1.005<prw");
		 rowdata.add((int)g_p5e + "个");
		 double g_p5f = Yz.Match_Condition(10, "np=-0.5");
		 rowdata.add((int)g_p5f + "个");
		 if(g_p5a > 3 && p5b.equals("Yes") && p5c.equals("胜") && g_p5d == 2 && g_p5e > 5 && g_p5f > 7){
			 if(colc == "") colc = "Z5"; else colc += "、Z5";
		 }
		 //项目6-Z9
		 double g_p6a = Fx.Get_List("zzs");
		 rowdata.add(g_p6a >= 0? (int)g_p6a + "场" : "N/A");
		 double g_p6b = Fx.Get_List("kks");
		 rowdata.add(g_p6b >= 0? (int)g_p6b + "场" : "N/A");
		 double g_p6c = Yz.Match_Condition(10, "pp=-0.25&1.005<prw");
		 rowdata.add((int)g_p6c + "个");
		 double g_p6d = Yz.Match_Condition(10, "np=-0.5");
		 rowdata.add((int)g_p6d + "个");
		 if(g_p6a == 0 && g_p6b > 2 && g_p6c > 5 && g_p6d > 7){
			 if(colc == "") colc = "Z9"; else colc += "、Z9";
		 }
		 //项目7-S3
		 double g_p7a = Yz.Match_Condition(12, "np=-1.5");
		 rowdata.add((int)g_p7a+"个");
		 double g_p7b = Dx.Match_Condition(12, "np=2.25");
		 rowdata.add((int)g_p7b+"个");
		 if(g_p7a > 6 && g_p7b > 1){
			 if(colc == "") colc = "S3"; else colc += "、S3";
		 }
		 //项目8-PF12
		 String p8a = Fx.Get_SF("s1", 0);
		 rowdata.add(p8a);
		 double g_p8b = Fx.Match_Condition(3, "s4");
		 rowdata.add((int)g_p8b + "场");
		 String p8c = Fx.Get_SF("s5", 0);
		 rowdata.add(p8c);
		 double g_p8d = Yz.Match_Condition(10, "pp=-0.25");
		 rowdata.add((int)g_p8d + "个");
		 double g_p8e = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p8e + "个");
		 if((p8a.equals("胜")|p8a.equals("平")) && g_p8b == 3 && p8c.equals("负") && g_p8d > 5 && g_p8e > 4){
			 if(colc == "") colc = "PF12"; else colc += "、PF12";
		 }
		 
		 //项目9-YQXP4
		 double g_p9a = Yz.Match_Condition(12, "np=-1&1.010<=nlw");
		 rowdata.add((int)g_p9a+"个");
		 double g_p9b = Yz.Match_Condition(12, "np=-0.75&1.010<=nlw");
		 rowdata.add((int)g_p9b+"个");
		 double g_p9c = Yz.Match_Condition(12, "np=-0.5&nlw<=0.78");
		 rowdata.add((int)g_p9c+"个");
		 double g_p9d = Fx.Match_Condition(2, "s5");
		 rowdata.add((int)g_p9d+"个");
		 if(g_p9a > 0 && g_p9b > 0 && g_p9c > 0 && g_p9d <= 1){
			 if(colc == "") colc = "YQXP4"; else colc += "、YQXP4";
		 }
		 
		 //项目10-Z19
		 String p10a = Fx.Get_SF("s4", 0);
		 rowdata.add(p10a);
		 double g_p10b = Yz.Match_Condition(10, "1.10<plw");
		 rowdata.add((int)g_p10b + "个");
		 double g_p10c = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p10b + "个");
		 double g_p10d = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p10d + "个");
		 if(p10a.equals("负") && g_p10b > 1 && g_p10c > 4 && g_p10d > 1){
			 if(colc == "") colc = "Z19"; else colc += "、Z19";
		 }
		 
		 //项目11-QDBS3
		 double g_p11a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 rowdata.add((int)g_p11a + "场");
		 double g_p11b = Yz.Match_Condition(10, "np=-1") + Yz.Match_Condition(10, "np=-1.25");
		 rowdata.add((int)g_p11b + "个");
		 double g_p11c = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p11c + "个");
		 String g_p11d = Fx.Get_SF("s5", 0);
		 rowdata.add(g_p11d);
		 if(g_p11a == 2 && g_p11b > 4 && g_p11c > 5 && g_p11d.equals("负")){
			 if(colc == "") colc = "QDBS3"; else colc += "、QDBS3";
		 }
		 
		 //项目12-YK
		 double g_p12a = Oz.Match_Condition(15, "pd<nd&nl<pl&nw=pw");
		 rowdata.add((int)g_p12a+"个");
		 if(g_p12a > 1){
			 if(colc == "") colc = "YK"; else colc += "、YK";
		 }
		 
		 //项目13-SF2
		 double g_p13a = Yz.Company_state("澳门", "np");
		 rowdata.add(g_p13a != -10? df.format(g_p13a) : "N/A");
		 double g_p13b = Oz.Company_state("澳门", "nd");
		 rowdata.add(g_p13b >= 0? df.format(g_p13b) : "N/A");
		 double g_p13c = Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(g_p13c >= 0? df.format(g_p13c) : "N/A");
		 if(g_p13a >= -0.25 && g_p13a <= 0.25 && g_p13b > 3.30 && g_p13c > 3.40){
			 if(colc == "") colc = "SF2"; else colc += "、SF2";
		 }
		 
		 //项目14-Z20
		 double g_p14a = Oz.Match_Condition(10, "nw=nl");
		 rowdata.add((int)g_p14a + "个");
		 double g_p14b = Oz.Match_Condition(10, "pw<nw");
		 rowdata.add((int)g_p14b + "个");
		 double g_p14c = Oz.Match_Condition(10, "nd<=pd");
		 rowdata.add((int)g_p14c + "个");
		 double g_p14d = Oz.Match_Condition(10, "nl<pl");
		 rowdata.add((int)g_p14d + "个");
		 String p14e = Fx.Get_SF("s4", 0);
		 rowdata.add(p14e);
		 if(g_p14a >= 1 && g_p14b > 5 && g_p14c > 5 && g_p14d > 5 && p14e.equals("胜")){
			 if(colc == "") colc = "Z20"; else colc += "、Z20";
		 }
		 
		 //项目15-DF2
		 double g_p15a = Dx.Match_Condition(20, "3.5<=pp");
		 rowdata.add((int)g_p15a+"个");
		 double g_p15b = Dx.Match_Condition(20, "3.5<=np&1.00<nd");
		 rowdata.add((int)g_p15b+"个");
		 if(g_p15a > 12 && g_p15b > 4){
			 if(colc == "") colc = "DF2"; else colc += "、DF2";
		 } 
		 
		 //项目16-DD2
		 double g_p16a = Fx.GP_Condition(5, "0<kg2");
		 rowdata.add((int)g_p16a+"场");
		 double g_p16b = Fx.GP_Condition(5, "0<zg3");
		 rowdata.add((int)g_p16b+"场");
		 double g_p16c = D[1];
		 rowdata.add((int)g_p16c+"个");
		 double g_p16d = Dx.Match_Condition(20, "1.00<nd");
		 rowdata.add((int)g_p16d+"个");
		 if(g_p16a >= 4 && g_p16b >= 4 && g_p16c > 1 && g_p16d > 10){
			 if(colc == "") colc = "DD2"; else colc += "、DD2";
		 }
				 
		 //项目17-DFF
		 double g_p17a = g_p16a;
		 rowdata.add((int)g_p17a+"场");
		 double g_p17b = g_p16b;
		 rowdata.add((int)g_p17b+"场");
		 double g_p17c = Dx.Match_Condition(15, "1.05<=nd");
		 rowdata.add((int)g_p17c+"个");
		 double g_p17d = Fx.Match_Condition((int)F[0], "x1");
		 rowdata.add((int)g_p17d+"场");
		 double g_p17e = Fx.Match_Condition(5, "x2") + Fx.Match_Condition(5, "x3");
		 rowdata.add((int)g_p17e+"场");
		 if(g_p17a >= 4 && g_p17b >= 4 && g_p17c >= 5 && (g_p17d > 4 | g_p17e >= 8)){
			 if(colc == "") colc = "DFF"; else colc += "、DFF";
		 }
		 
		 //项目18-K1
		 double g_p18a = Oz.Match_Condition(10, "nw=nl");
		 rowdata.add((int)g_p18a + "个");
		 double g_p18b = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p18b + "个");
		 double g_p18c = Oz.Match_Condition(10, "nd<=pd");
		 rowdata.add((int)g_p18c + "个");
		 double g_p18d = Oz.Match_Condition(10, "pl<nl");
		 rowdata.add((int)g_p18d + "个");
		 String p18e = Fx.Get_SF("s5", 0);
		 rowdata.add(p18e);
		 if(g_p18a >= 1 && g_p18b > 5 && g_p18c > 5 && g_p18d > 5 && p18e.equals("胜")){
			 if(colc == "") colc = "K1"; else colc += "、K1";
		 }
				 
 		 //项目19-Z1
		 double g_p19a = F[0] - Fx.Match_Condition((int)F[0], "s1");
		 rowdata.add((int)g_p19a+"场");
		 double g_p19b = 5-Fx.Match_Condition(5, "s2");
		 rowdata.add((int)g_p19b+"场");
		 double g_p19c = Oz.Match_Condition(20, "nl=nd");
		 rowdata.add((int)g_p19c+"个");
		 double g_p19d = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p19d+"个");
		 if((int)g_p19a > 5 && g_p19b == 5 && g_p19c >= 1 && g_p19d < 2){
			 if(colc == "") colc = "Z1"; else colc += "、Z1";
		 }
				 
		 //项目20-ZP3
		 double g_p20a = Yz.Company_state("Interwetten", "np");
		 rowdata.add(g_p20a != -10? df.format(g_p20a):"-");
		 double g_p20b = Yz.Company_state("Interwetten", "nrw");
		 rowdata.add(g_p20b != -10? df.format(g_p20b):"-");
		 double g_p20c = Oz.Company_state("Interwetten", "nd");
		 rowdata.add(df.format(g_p20c));
		 if(g_p20a == 0 && g_p20b >= 1.300 && g_p20c >=3.40){
			 if(colc == "") colc = "ZP3"; else colc += "、ZP3";
		 }
		 
		 //项目21-Z21
		 String p21a = Fx.Get_SF("f4", 0);
		 rowdata.add(p21a);
		 String p21b = Fx.Get_SF("s5", 0);
		 rowdata.add(p21b);
		 double g_p21c = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p21c + "个");
		 double g_p21d = Y[9];
		 rowdata.add((int)g_p21d + "个");
		 if(p21a.equals("负") && p21b.equals("胜") && g_p21c > 2 && g_p21d > 5){
			 if(colc == "") colc = "Z21"; else colc += "、Z21";
		 }
		 
		 //项目22-Z6
		 double g_p22a = Fx.Match_Condition(5, "p4");
		 rowdata.add((int)g_p22a+"场");
		 double g_p22b = Fx.Match_Condition(5, "s5") + Fx.Match_Condition(5, "p5");
		 rowdata.add((int)g_p22b+"场");
		 double g_p22c = Oz.Match_Condition(10, "3.39<nd");
		 rowdata.add((int)g_p22c+"个");
		 double g_p22d = Oz.Match_Condition(10, "nd=nl");
		 rowdata.add((int)g_p22d+"个");
		 double g_p22e = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p22e+"个");
		 if(g_p22a >= 3 && g_p22b == 5 && g_p22c > 2 && g_p22d >= 1 && g_p22e > 1){
			 if(colc == "") colc = "Z6"; else colc += "、Z6";
		 }
				 
		 //项目23-PB
		 double g_p23a = Oz.Average_Odds(4,"n")[0];
		 rowdata.add(df.format(g_p23a)); //1.80<=x<=2.0);
		 double g_p23b = Oz.Match_Condition(20, "nd<=3.30");
		 rowdata.add((int)g_p23b+"个");
		 double g_p23c = O[1];
		 rowdata.add(df.format(g_p23c));
		 double g_p23d = Fx.Match_Condition(3, "f2");
		 rowdata.add((int)g_p23d+"场");
		 double g_p23e = Fx.Match_Condition(3, "s3");
		 rowdata.add((int)g_p23e+"场");
		 double g_p23f = 5-Fx.Match_Condition(5, "s2");
		 rowdata.add((int)g_p23f+"场");
		 String p23g = Fx.Get_SF("s5", 0);
		 rowdata.add(p23g);
		 if(g_p23a >= 1.80 && g_p23a <= 2.00 && g_p23b > 7 && g_p23c > 0.6 && g_p23d >= 1 && g_p23e >= 1 && g_p23f >= 3 && p23g.equals("胜")){
			 if(colc == "") colc = "PB"; else colc += "、PB";
		 }
		 
		 //项目24-KS1
		 double g_p24a = Oz.Match_Condition(20, "pw<nw&nd<pd&pl<nl");
		 rowdata.add((int)g_p24a + "个");
		 double g_p24b = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl");
		 rowdata.add((int)g_p24b + "个");
		 double g_p24c = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p24c + "个");
		 double g_p24d = l;
		 rowdata.add(df.format(g_p24d));
		 if(g_p24a > 5 && g_p24b > 5 && g_p24c >= 2 && g_p24d > 4.00){
			 if(colc == "") colc = "KS1"; else colc += "、KS1";
		 }
		 
		 //项目25-SF5
		 double g_p25a = Fx.Match_Condition(5, "p4");
		 rowdata.add((int)g_p25a + "场");
		 double g_p25b = Oz.Match_Condition(10, "3.49<nd");
		 rowdata.add((int)g_p25b + "个");
		 if(g_p25a >= 3 && g_p25b > 2){
			 if(colc == "") colc = "SF5"; else colc += "、SF5";
		 }
		 
		 //项目26-Z22
		 String p26a = Fx.Get_SF("f4", 0);
		 rowdata.add(p26a);
		 String p26b = Fx.Get_SF("s5", 0);
		 rowdata.add(p26b);
		 double g_p26c = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p26c + "个");
		 double g_p26d = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p26d + "个");
		 double g_p26e = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p26e + "个");
		 if(p26a.equals("负") && p26b.equals("胜") && g_p26c > 5 && g_p26d > 5 && g_p26e > 2){
			 if(colc == "") colc = "Z22"; else colc += "、Z22";
		 }
		 
		 //项目27-2P1
		 rowdata.add(Temp.Bet365P == -10? "-":PanK[(int)((Temp.Bet365P + 4) / 0.25)]);
		 rowdata.add(df.format(Temp.WiliamD)+"");
		 
		 //项目28-PF4
		 double g_p28a = Fx.Get_List("kks");
		 rowdata.add(g_p28a >= 0? (int)g_p28a + "场" : "-");
		 double g_p28b = Oz.Match_Condition(10, "nl<pl");
		 rowdata.add((int)g_p28b + "个");
		 double g_p28c = Oz.Match_Condition(10, "3.25<nd");
		 rowdata.add((int)g_p28c + "个");
		 double g_p28d = Yz.Match_Condition(10, "np=0.25&1.005<nrw");
		 rowdata.add((int)g_p28d + "个");
		 if(g_p28a == 0 && g_p28b > 7 && g_p28c > 4 && g_p28d > 6){
			 if(colc == "") colc = "PF4"; else colc += "、PF4";
		 }
		 
		 //项目29-PF1
		 double g_p29a = 10-Fx.Match_Condition(10, "f2");
		 rowdata.add((int)g_p29a+"场");
		 double g_p29b = AveO20[0]; //>2.00
		 rowdata.add(df.format(g_p29b)+"");
		 double g_p29c = AveO20[1]; //<3.30
		 rowdata.add(df.format(g_p29c)+"");
		 double g_p29d = Yz.Match_Condition(10, "-0.25<=np&1.020<=nlw");
		 rowdata.add((int)g_p29d+"个");
		 double g_p29e = Oz.Match_Condition(10, "nw<=pw");
		 rowdata.add((int)g_p29e+"个");
		 double g_p29f = Fx.Match_Condition(4, "f5");
		 rowdata.add((int)g_p29f+"场");
		 double g_p29g = 4-Fx.Match_Condition(4, "f4");
		 rowdata.add((int)g_p29g+"场");
		 double g_p29h = Oz.Company_state("威廉希尔", "nd"); //>3.00
		 rowdata.add(df.format(g_p29h)+"");
		 double g_p29i = Oz.Company_state("Ladbrokes (立博)", "nd"); //>3.00
		 rowdata.add(df.format(g_p29i)+"");
		 double g_p29j = Math.abs(Fx.Get_Rank("z")-Fx.Get_Rank("k"));
		 if(Fx.Get_Rank("z")*Fx.Get_Rank("k") == 0) g_p29j = -1;
		 rowdata.add((int)g_p29j+"");
		 if(g_p29a >= 8 && g_p29b > 2.00 && g_p29c < 3.30 && g_p29d >= 2 && g_p29e >= 7 && g_p29f >= 2 && g_p29g == 4 && g_p29h > 3.00 && g_p29i > 3.00 && g_p29j > 5){
			 if(colc == "") colc = "PF1"; else colc += "、PF1";
		 }
		 
		 //项目30-YQXP2
		 String g_p30a = Fx.Get_SF("s1", 0);
		 rowdata.add(g_p30a);
		 double g_p30b = Fx.Series(10, "s4");
		 rowdata.add((int)g_p30b + "场");
		 String g_p30c = Fx.Get_SF("f5", 0);
		 rowdata.add(g_p30c);
		 double g_p30d = Yz.Match_Condition(10, "np=-1&1.005<nlw");
		 rowdata.add((int)g_p30d + "个");
		 if(g_p30a.equals("胜") && g_p30b >= 3 && g_p30c.equals("负") && g_p30d > 4){
			 if(colc == "") colc = "YQXP2"; else colc += "、YQXP2";
		 }
		 
		 //项目31-PF10
		 double g_p31a = Fx.Get_List("kks");
		 rowdata.add(g_p31a >= 0? (int)g_p31a + "场" : "-");
		 double g_p31b = Oz.Match_Condition(10, "pw<nw");
		 rowdata.add((int)g_p31b + "个");
		 double g_p31c = Yz.Match_Condition(10, "np=-0.25&1.005<nrw");
		 rowdata.add((int)g_p31c + "个");
		 if(g_p31a == 0 && g_p31b > 7 && g_p31c > 7){
			 if(colc == "") colc = "PF10"; else colc += "、PF10";
		 }
		 
		 //项目32-Z15
		 double g_p32a = Fx.Match_Condition(3, "p1") + Fx.Match_Condition(3, "f1");
		 rowdata.add((int)g_p32a + "场");
		 double g_p32b = Fx.Match_Condition(3, "p2") + Fx.Match_Condition(3, "f2");
		 rowdata.add((int)g_p32b + "场");
		 double g_p32c = Fx.Match_Condition(3, "s4") + Fx.Match_Condition(3, "p4");
		 rowdata.add((int)g_p32c + "场");
		 double g_p32d = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p32d + "个");
		 double g_p32e = Oz.Match_Condition(10, "pw<nw");
		 rowdata.add((int)g_p32e + "个");
		 double g_p32f = Oz.Match_Condition(10, "nd=nl");
		 rowdata.add((int)g_p32f + "个");
		 if(g_p32a == 3 && g_p32b == 3 && g_p32c == 3 && g_p32d > 4 && g_p32e > 5 && g_p32f > 2){
			 if(colc == "") colc = "Z15"; else colc += "、Z15";
		 }
		 
		 //项目33-K
		 double p33a1 = Oz.Company_state("Ladbrokes (立博)", "nd");
		 double p33a2 = Oz.Company_state("威廉希尔", "nd");
		 double g_p33a = p33a1 - p33a2;
		 rowdata.add(p33a1 != -10 && p33a2 != -10? df.format(g_p33a):"N/A"); // >0.35
		 double g_p33b = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p33b + "个");
		 double g_p33c = Y[7];
		 rowdata.add((int)g_p33c + "个");
		 double g_p33d = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p33d + "个");
		 String g_p33e = Fx.Get_SF("s4", 0);
		 rowdata.add(g_p33e);
		 String g_p33f = Fx.Get_SF("s5", 0);
		 rowdata.add(g_p33f);
		 if(g_p33a > 0.35 && g_p33b > 5 && g_p33c > 5 && g_p33d >= 1 && (g_p33e.equals("平") | g_p33e.equals("负")) && (g_p33f.equals("平") | g_p33f.equals("负"))){
			 if(colc == "") colc = "K"; else colc += "、K";
		 }
		 
		 
		 //项目34-YQXP1
		 double g_p34a = Yz.Match_Condition(15, "np=-1.5&1.000<=nlw");
		 rowdata.add((int)g_p34a+"个");
		 double g_p34b = Dx.Match_Condition(15, "1.000<=nd");
		 rowdata.add((int)g_p34b+"个");
		 if(g_p34a >= 7 && g_p34b >= 7){
			 if(colc == "") colc = "YQXP1"; else colc += "、YQXP1";
		 }
				 
		 //项目35-SF1
		 double g_p35a = Fx.Match_Condition(4, "s1");
		 rowdata.add((int)g_p35a+"场");
		 double g_p35b = 5-Fx.Match_Condition(5, "f2");
		 rowdata.add((int)g_p35b+"场");
		 double g_p35c = Yz.Match_Condition(15, "pp<np&1.000<=nlw&1.000<=plw");
		 rowdata.add((int)g_p35c+"个");
		 double g_p35d = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl");
		 rowdata.add((int)g_p35d+"个");
		 if(g_p35a == 4 && g_p35b == 5 && g_p35c > 4 && g_p35d > 8){
			 if(colc == "") colc = "SF1"; else colc += "、SF1";
		 }
		 
		 //项目36-YQXP3
		 String g_p36a = Fx.Get_SF("s4", 0);
		 rowdata.add(g_p36a);
		 String g_p36b = Fx.Get_SF("s5", 0);
		 rowdata.add(g_p36b);
		 String g_p36c = Fx.Get_SF("s1", 0);
		 rowdata.add(g_p36c);
		 double g_p36d = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p36d + "个");
		 double g_p36e = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p36e + "个");
		 if(g_p36a.equals("胜") && g_p36b.equals("负") && g_p36c.equals("胜") && g_p36d > 5 && g_p36e > 4){
			 if(colc == "") colc = "YQXP3"; else colc += "、YQXP3";
		 }
				 
		 //项目37-K2
		 double g_p37a = Yz.Match_Condition(10, "np=0.5&1.000<=nrw");
		 rowdata.add((int)g_p37a+"个");
		 double g_p37b = Oz.Match_Condition(20, "nd=nw");
		 rowdata.add((int)g_p37b+"个");
		 double g_p37c = Fx.Match_Condition(5, "s4");
		 rowdata.add((int)g_p37c+"场");
		 double g_p37d = Fx.Match_Condition(5, "f5");
		 rowdata.add((int)g_p37d+"场");
		 if(g_p37a >= 3 && g_p37b >= 1 && g_p37c >= 3 && g_p37d >= 3){
			 if(colc == "") colc = "K2"; else colc += "、K2";
		 }
		 
		 //项目38-Z16
		 double g_p38a = w;
		 rowdata.add(df.format(g_p38a));
		 double g_p38b = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl&nd=nl");
		 rowdata.add((int)g_p38b + "个");
		 double g_p38c = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p38c + "个");
		 if(g_p38a < 2.0 && g_p38a > 0 && g_p38b > 1 && g_p38c > 2){
			 if(colc == "") colc = "Z16"; else colc += "、Z16";
		 }
		 
		 //项目39-Z23
		 String p39a = Fx.Get_SF("f4", 0);
		 rowdata.add(p39a);
		 String p39b = Fx.Get_SF("s5", 0);
		 rowdata.add(p39b);
		 double g_p39c = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p39c + "个");
		 double g_p39d = Y[10];
		 rowdata.add((int)g_p39d + "个");
		 double g_p39e = Oz.Match_Condition(10, "nw=nl");
		 rowdata.add((int)g_p39e + "个");
		 double g_p39f = Oz.Match_Condition(10, "nw=nd");
		 rowdata.add((int)g_p39f + "个");
		 if(p39a.equals("负") && p39b.equals("胜") && g_p39c > 5 && g_p39d > 5 && g_p39e >= 1 && g_p39f >= 1){
			 if(colc == "") colc = "Z23"; else colc += "、Z23";
		 }
				 
		 //项目40-PF2
		 double g_p40a = Fx.Match_Condition(3, "s4");
		 rowdata.add((int)g_p40a+"场");
		 String p40b = Fx.Get_SF("f5", 0);
		 rowdata.add(p40b);
		 double g_p40c = Oz.Match_Condition(20, "nw<pw");
		 rowdata.add((int)g_p40c+"个");
		 double g_p40d = Yz.Match_Condition(12, "pp=-0.25&np=-0.5");
		 rowdata.add((int)g_p40d+"个");
		 double g_p40e = Yz.Match_Condition(12, "1.000<nlw");
		 rowdata.add((int)g_p40e+"个");
		 if(g_p40a == 3 && p40b.equals("负") && g_p40c > 12 && g_p40d > 5 && g_p40e > 5){
			 if(colc == "") colc = "PF2"; else colc += "、PF2";
		 }
				 
		 //项目41-PG
		 double g_p41a = AveO10[0];
		 rowdata.add(df.format(g_p41a)); //>2.0);
		 double g_p41b = AveO10[2];
		 rowdata.add(df.format(g_p41b)); //>2.0);
		 double g_p41c = Oz.Match_Condition(10, "3.50<nd");
		 rowdata.add((int)g_p41c+"个");
		 if(g_p41a > 2.00 && g_p41b > 2.00 && g_p41c >= 2){
			 if(colc == "") colc = "PG"; else colc += "、PG";
		 }
				 
		 //项目42-PG2
		 double g_p42a = AveO10[0];
		 rowdata.add(df.format(g_p42a)); //>1.7);
		 double g_p42b = AveO10[2];
		 rowdata.add(df.format(g_p42b)); //>2.0);
		 double g_p42c = Oz.Match_Condition(10, "3.80<=nd");
		 rowdata.add((int)g_p42c+"个");
		 if(g_p42a > 1.70 && g_p42b > 2.00 && g_p42c >= 2){
			 if(colc == "") colc = "PG2"; else colc += "、PG2";
		 }
		 
		 //项目43-KS
		 double g_p43a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 rowdata.add((int)g_p43a + "场");
		 double g_p43b = Fx.Match_Condition(2, "p5") + Fx.Match_Condition(2, "f5");
		 rowdata.add((int)g_p43b + "场");
		 double g_p43c = Oz.Match_Condition(20, "nw<pw");
		 rowdata.add((int)g_p43c + "个");
		 double g_p43d = Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(df.format(g_p43d)); // >3.30
		 double g_p43e = Yz.Match_Condition(10, "np=-0.25");
		 rowdata.add((int)g_p43e + "个");
		 double g_p43f = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p43f + "个");
		 if(g_p43a == 2 && g_p43b == 2 && g_p43c > 10 && g_p43d > 3.30 && g_p43e >= 5 && g_p43f >= 5){
			 if(colc == "") colc = "KS"; else colc += "、KS";
		 }
		 
				 
		 //项目44-SNAI
		 double g_p44a;
		 int a = 0,b = 0;
		 double maxf = Oz.Max_Min(30, "nl")[0];
		 double maxs = Oz.Max_Min(30, "nw")[0];
		 if(maxf == Oz.Company_state("SNAI", "nl")) a = 1;
		 if(maxs == Oz.Company_state("SNAI", "nw")) b = 1;
		 g_p44a = b*2+a;
		 String p44a = "";
		 switch((int)g_p44a){
		 case 0:p44a = "No";break;
		 case 1:p44a = "负赔率最高";break;
		 case 2:p44a = "胜赔率最高";break;
		 case 3:p44a = "胜负赔率均为最高";break;
		 default:p44a = "";
		 }
		 rowdata.add(p44a);
		 if(g_p44a > 0){
			 if(colc == "") colc = "SNAI"; else colc += "、SNAI";
		 }
				 
		 //项目45-WL
		 double g_p45a;
		 a = 0;b = 0;
		 if(maxf == Oz.Company_state("威廉希尔", "nl")) a = 1;
		 if(maxs == Oz.Company_state("威廉希尔", "nw")) b = 1;
		 g_p45a = b*2+a;
		 String p45a = "";
		 switch((int)g_p45a){
		 case 0:p45a = "No";break;
		 case 1:p45a = "负赔率最高";break;
		 case 2:p45a = "胜赔率最高";break;
		 case 3:p45a = "胜负赔率均为最高";break;
		 default:p45a = "";
		 }
		 rowdata.add(p45a);
		 if(g_p45a > 0){
			 if(colc == "") colc = "WL"; else colc += "、WL";
		 }
				 
		 //项目46-Oddset
		 double g_p46a;
		 a = 0;b = 0;
		 if(maxf == Oz.Company_state("Oddset (奥德赛特)", "nl")) a = 1;
		 if(maxs == Oz.Company_state("Oddset (奥德赛特)", "nw")) b = 1;
		 g_p46a = b*2+a;
		 String p46a = "";
		 switch((int)g_p46a){
		 case 0:p46a = "No";break;
		 case 1:p46a = "负赔率最高";break;
		 case 2:p46a = "胜赔率最高";break;
		 case 3:p46a = "胜负赔率均为最高";break;
		 default:p46a = "";
		 }
		 rowdata.add(p46a);
		 if(g_p46a > 0){
			 if(colc == "") colc = "Oddset"; else colc += "、Oddset";
		 }
		 
		 //项目47-Z12
		 double g_p47a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 rowdata.add((int)g_p47a + "场");
		 String g_p47b = Fx.Get_SF("s5", 0);
		 rowdata.add(g_p47b);
		 double g_p47c = Fx.Series(5, "f1");
		 rowdata.add((int)g_p47c + "场");
		 double g_p47d = Yz.Match_Condition(10, "np=-1 or np=-1.25");
		 rowdata.add((int)g_p47d + "个");
		 double g_p47e = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p47e + "个");
		 if(g_p47a == 2 && g_p47b.equals("胜") && g_p47c >= 2 && g_p47d > 4 && g_p47e > 5){
			 if(colc == "") colc = "Z12"; else colc += "、Z12";
		 }
		 
		 //项目48-Z24
		 String p48a = Fx.Get_SF("f4", 0);
		 rowdata.add(p48a);
		 String p48b = Fx.Get_SF("f5", 0);
		 rowdata.add(p48b);
		 double g_p48c = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p48c + "个");
		 double g_p48d = Y[9];
		 rowdata.add((int)g_p48d + "个");
		 if(p48a.equals("负") && p48b.equals("负") && g_p48c < 1 && g_p48d > 5){
			 if(colc == "") colc = "Z24"; else colc += "、Z24";
		 }
		 
		 //项目49-Z25
		 String p49a = Fx.Get_SF("f4", 0);
		 rowdata.add(p49a);
		 String p49b = Fx.Get_SF("p5", 0);
		 rowdata.add(p49b);
		 double g_p49c = Oz.Match_Condition(10, "nl<pl");
		 rowdata.add((int)g_p49c + "个");
		 double g_p49d = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p49d + "个");
		 double g_p49e = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p49e + "个");
		 double g_p49f1 = Oz.Company_state("威廉希尔", "nd");
		 double g_p49f2 = Oz.Company_state("立博", "nd");
		 double g_p49f = g_p49f1 != 0 && g_p49f2 != 0? g_p49f1 - g_p49f2 : -1;
		 rowdata.add(g_p49f1 != 0 && g_p49f2 != 0? df.format(g_p49f):"-");
		 if(p49a.equals("负") && p49b.equals("平") && g_p49c > 5 && g_p49d > 4 && g_p49e > 4 && g_p49f > 0){
			 if(colc == "") colc = "Z25"; else colc += "、Z25";
		 }
		 
		 //项目50-PF9
		 double g_p50a = Fx.Match_Condition(2, "s4");
		 rowdata.add((int)g_p50a + "场");
		 double g_p50b = Yz.Match_Condition(10, "pp<np&1.005<nlw&1.005<plw");
		 rowdata.add((int)g_p50b + "个");
		 double g_p50c = Oz.Match_Condition(10, "pw<nw");
		 rowdata.add((int)g_p50c + "个");
		 if(g_p50a == 2 && g_p50b >= 2 && g_p50c > 5){
			 if(colc == "") colc = "PF9"; else colc += "、PF9";
		 }
				 
		 //项目51-2QXPSP
		 double g_p51a = Yz.Match_Condition(10, "np<=-1.5");
		 rowdata.add((int)g_p51a+"个");
		 double g_p51b = Dx.Match_Condition(10, "np<=2.5");
		 rowdata.add((int)g_p51b+"个");
		 double g_p51c = Fx.Match_Condition(4, "d4");
		 rowdata.add((int)g_p51c+"个");
		 if(g_p51a > 6 && g_p51b > 7 && g_p51c >= 2){
			 if(colc == "") colc = "2QXPSP"; else colc += "、2QXPSP";
		 }
		 
		 //项目52-Z14
		 double g_p52a = Fx.Match_Condition(3, "p4") + Fx.Match_Condition(3, "f4");
		 rowdata.add((int)g_p52a + "场");
		 double g_p52b = Yz.Match_Condition(10, "pp=-0.5&1.005<plw&np=-0.25&1.005<nlw");
		 rowdata.add((int)g_p52b + "个");
		 double g_p52c = Yz.Match_Condition(10, "np=0&nlw<0.8");
		 rowdata.add((int)g_p52c + "个");
		 if(g_p52a == 3 && g_p52b > 2 && g_p52c >= 1){
			 if(colc == "") colc = "Z14"; else colc += "、Z14";
		 }
				 
		 //项目53-ZP4
		 double g_p53a = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p53a + "个");
		 double g_p53b = Yz.Match_Condition(10, "np<pp&1.005<nlw&1.005<plw");
		 rowdata.add((int)g_p53b + "个");
		 String p53c = Fx.Get_SF("s1", 0);
		 rowdata.add(p53c);
		 String p53d = Fx.Get_SF("s4", 0);
		 rowdata.add(p53d);
		 String p53e = Fx.Get_SF("f5", 0);
		 rowdata.add(p53e);
		 if(g_p53a >= 5 && g_p53b > 3 && p53c.equals("胜") && p53d.equals("胜") && p53e.equals("负")){
			 if(colc == "") colc = "ZP4"; else colc += "、ZP4";
		 }
		 
		 //项目54-ZP20
		 String p54a = Fx.Get_SF("f4", 0);
		 rowdata.add(p54a);
		 String p54b = Fx.Get_SF("s5", 0);
		 rowdata.add(p54b);
		 double g_p54c = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p54c + "个");
		 double g_p54d = Y[10];
		 rowdata.add((int)g_p54d + "个");
		 if(p54a.equals("负") && p54b.equals("胜") && g_p54c > 5 && g_p54d > 5){
			 if(colc == "") colc = "ZP20"; else colc += "、ZP20";
		 }
		 
		 //项目56-ZP24(QDXP)
		 String g_p56a = Fx.Get_SF("s4", 0);
		 rowdata.add(g_p56a);
		 String g_p56b = Fx.Get_SF("s5", 0);
		 rowdata.add(g_p56b);
		 double g_p56c = Fx.Get_Rank("z");
		 rowdata.add((int)g_p56c + "");
		 double g_p56d = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p56d + "个");
		 double g_p56e = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p56e + "个");
		 double g_p56f = Y[8];
		 rowdata.add((int)g_p56f + "个");
		 if(g_p56a.equals("胜") && (g_p56b.equals("负") | g_p56b.equals("平")) && g_p56c <= 2 && g_p56c > 0 && g_p56d > 5 && 
				 g_p56e >= 1 && g_p56f >= 1){
			 if(colc == "") colc = "ZP24(QDXP)"; else colc += "、ZP24(QDXP)";
		 }
		 
		 //项目57-Z17
		 double g_p57a = Fx.Get_Rank("zd");
		 rowdata.add(g_p57a == -1? "N/A":(int)g_p57a + "");
		 double g_p57b = Yz.Match_Condition(10, "pp=-0.25&plw<0.9&np=-0.75&1.005<nlw");
		 rowdata.add((int)g_p57b + "个");
		 if(g_p57a > 0 && g_p57a < 4 && g_p57b > 2){
			 if(colc == "") colc = "Z17"; else colc += "、Z17";
		 }
				 
		 //项目58-ZP5
		 double g_p58a = Fx.Match_Condition((int)F[0], "f1");
		 rowdata.add((int)g_p58a == 0 && F[0] > 2? "Yes":"No");
		 double g_p58b = g_p53a;
		 rowdata.add((int)g_p58b+"个");
		 double g_p58c = Oz.Match_Condition(10, "3.5<=nd");
		 rowdata.add((int)g_p58c+"个");
		 double g_p58d = Yz.Match_Condition(12, "pp=-0.25&np=-0.5");
		 rowdata.add((int)g_p58d+"个");
		 double g_p58e = Fx.Match_Condition(4, "f5");
		 rowdata.add((int)g_p58e+"场");
		 String p58f = Fx.Get_SF("f5", 0);
		 rowdata.add(p58f);
		 String p58g = Fx.Get_SF("s4", 0);
		 rowdata.add(p58g);
		 if(g_p58a == 0 && F[0] > 2 && g_p58b >= 6 && g_p58c >= 1 && g_p58d > 4 && g_p58e > 1 && p58f.equals("负") && p58g.equals("胜")){
			 if(colc == "") colc = "ZP5"; else colc += "、ZP5";
		 }
		 
		 //项目59-KS2
		 double g_p59a = Oz.Match_Condition(20, "pw<nw&nd<pd&pl<nl");
		 rowdata.add((int)g_p59a+"个");
		 double g_p59b = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl");
		 rowdata.add((int)g_p59b+"个");
		 double g_p59c = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p59c+"个");
		 double g_p59d = l;
		 rowdata.add(df.format(g_p59d));
		 double g_p59e = Oz.Match_Condition(20, "nd=nl");
		 rowdata.add((int)g_p59e+"个");
		 if(g_p59a > 5 && g_p59b > 5 && g_p59c >= 2 && g_p59d > 4.00 && g_p59e > 1){
			 if(colc == "") colc = "KS2"; else colc += "、KS2";
		 }
		 
		 //项目62-Z18
		 double g_p62a = Fx.Match_Condition(3, "p5") + Fx.Match_Condition(3, "s5");
		 rowdata.add((int)g_p62a + "场");
		 double g_p62b = Fx.Get_Rank("zd");
		 rowdata.add(g_p62b == -1? "N/A":(int)g_p62b + ""); 
		 double g_p62c = Yz.Match_Condition(10, "pp=-0.5&1.005<plw&np=-0.75&1.005<nlw");
		 rowdata.add((int)g_p62c + "个");
		 if(g_p62a == 3 && g_p62b > 0 && g_p62b < 4 && g_p62c > 2){
			 if(colc == "") colc = "Z18"; else colc += "、Z18";
		 }
		 
		 //项目63-SF
		 double g_p63a = Fx.Match_Condition((int)F[0], "p1");
		 rowdata.add((int)g_p63a+"场");
		 double g_p63b = Oz.Company_state("威廉希尔", "pd") - Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(df.format(g_p63b)); //>0.);
		 double g_p63c = Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(df.format(g_p63c)); //2.90<x<3.6);
		 double g_p63d = Oz.Company_state("威廉希尔", "nl");
		 rowdata.add(df.format(g_p63d)); //>1.8);
		 if(g_p63a >= 3 && g_p63b > 0.3 && g_p63c > 2.90 && g_p63c < 3.60 && g_p63d > 1.80){
			 if(colc == "") colc = "SF"; else colc += "、SF";
		 }
				 
		 //项目64-QDSP
		 double g_p64a = Y[3];
		 rowdata.add((int)g_p64a+"个");
		 double g_p64b = D[2];
		 rowdata.add((int)g_p64b+"个");
		 if(g_p64a > 4 && g_p64b > 4){
			 if(colc == "") colc = "QDSP"; else colc += "、QDSP";
		 }
		 
		 //项目65-ZP6
		 double g_p65a = Fx.Get_Rank("zd");
		 rowdata.add(g_p65a == -1? "N/A":(int)g_p65a + "");
		 double g_p65b = Yz.Match_Condition(10, "np=-0.5");
		 rowdata.add((int)g_p65b + "个");
		 double g_p65c = Oz.Match_Condition(10, "nd=nl");
		 rowdata.add((int)g_p65c + "个");
		 if(g_p65a == 1 && g_p65b > 3 && g_p65c == 0){
			 if(colc == "") colc = "ZP6"; else colc += "、ZP6";
		 }
		  
		 //项目68-QDBS2
		 double g_p68a = Fx.Match_Condition(4, "s2");
		 rowdata.add((int)g_p68a+"场");
		 double g_p68b = Fx.Match_Condition(4, "f3");
		 rowdata.add((int)g_p68b+"场");
		 double g_p68c = Yz.Match_Condition(10, "pp=-1&plw<0.80");
		 rowdata.add((int)g_p68c+"个");
		 double g_p68d = Yz.Match_Condition(10, "pp=-1.25&1.005<plw");
		 rowdata.add((int)g_p68d+"个");
		 String p68e = Fx.Get_SF("s1", 0);
		 rowdata.add(p68e);
		 if(g_p68a >= 3 && g_p68b >= 3 && g_p68c >= 2 && g_p68d >= 2 && p68e.equals("胜")){
			 if(colc == "") colc = "QDBS2"; else colc += "、QDBS2";
		 }
				 
		 //项目70-ZP13
		 double g_p70a = Oz.Match_Condition(20, "nd=nl&3.29<nd&3.29<nl");
		 rowdata.add((int)g_p70a+"个");
		 double g_p70b = Fx.Match_Condition(3, "s5");
		 rowdata.add((int)g_p70b+"场");
		 double g_p70c = Fx.Match_Condition(4, "s3");
		 rowdata.add((int)g_p70c+"场");
		 String p70d = Fx.Get_SF("s3", 0);
		 rowdata.add(p70d);
		 double g_p70e = Fx.Match_Condition(3, "f4");
		 rowdata.add((int)g_p70e+"场");
		 double g_p70f = Fx.Match_Condition(4, "f2");
		 rowdata.add((int)g_p70f+"场");
		 String p70g = Fx.Get_SF("s2", 0);
		 rowdata.add(p70g);
		 String p70h = Fx.Get_SF("s4", 0);
		 rowdata.add(p70h);
		 String p70i = Fx.Get_SF("s5", 0);
		 rowdata.add(p70i);
		 if(g_p70a >= 2 && g_p70b >= 1 && g_p70c >= 2 && p70d.equals("胜") && g_p70e >= 1 && g_p70f >= 2 && p70g.equals("负") &&
				 p70h.equals("负") && p70i.equals("胜")){
			 if(colc == "") colc = "ZP13"; else colc += "、ZP13";
		 }
				 
		 //项目71-PF5
		 double g_p71a = AveO10[0];
		 rowdata.add(df.format(g_p71a)); //＞2.00，＜4.6);
		 double g_p71b = AveO10[2];
		 rowdata.add(df.format(g_p71b)); //1.60<=x<=2.1);
		 double g_p71c = g_p41c;
		 rowdata.add((int)g_p71c+"个");
		 double g_p71d = Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(df.format(g_p71d)); //3.49<=x<=3.8);
		 double g_p71e = g_p19a;
		 rowdata.add(g_p71e == F[0] && F[0] > 2? "Yes":"No");
		 double g_p71f = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p71f + "个");
		 String p71g = Fx.Get_SF("f5", 0);
		 rowdata.add(p71g);
		 double g_p71h = Oz.Match_Condition(10, "nl<pl&pd<nd");
		 rowdata.add((int)g_p71h + "个");
		 if(g_p71a > 2.00 && g_p71a < 4.60 && g_p71b >= 1.60 && g_p71b <= 2.10 && g_p71c >= 2 && g_p71d >= 3.49 && g_p71d <= 3.81 && 
				 g_p71e == F[0] && F[0] > 2 && g_p71f > 3 && p71g.equals("胜") && g_p71h > 4){
			 if(colc == "") colc = "PF5"; else colc += "、PF5";
		 }
				 
		 //项目72-D1
		 double g_p72a = g_p17d;
		 rowdata.add(g_p72a == F[0] && F[0] > 2? "Yes":"No");
		 double g_p72b = Dx.Match_Condition(15, "np<=2.5&1.005<nx");
		 rowdata.add((int)g_p72b+"个");
		 if(g_p72a == F[0] && F[0] > 2 && g_p72b > 6){
			 if(colc == "") colc = "D1"; else colc += "、D1";
		 }
				 
		 //项目73-D2
		 double g_p73a = Fx.Series(10, "x4");
		 rowdata.add((int)g_p73a+"场");
		 double g_p73b = Fx.Series(10, "x5");
		 rowdata.add((int)g_p73b+"场");
		 String p73c = Fx.Get_SF("x4", 0);
		 rowdata.add(p73c);
		 String p73d = Fx.Get_SF("x5", 0);
		 rowdata.add(p73d);
		 double g_p73e = Dx.Match_Condition(20, "1.00<px");
		 rowdata.add((int)g_p73e+"个");
		 if((g_p73a > 4 | g_p73b > 4) && p73c.equals("小") && p73d.equals("小") && g_p73e > 10){
			 if(colc == "") colc = "D2"; else colc += "、D2";
		 }
				 
		 //项目76-PF6
		 double g_p76a = AveO10[2];
		 rowdata.add(df.format(g_p76a)); //1.75<=x<=2.0);
		 double g_p76b = O[9];
		 rowdata.add(df.format(g_p76b)); //>0.);
		 double g_p76c = Fx.Match_Condition(3, "f3");
		 rowdata.add((int)g_p76c+"场");
		 String p76d = Fx.Get_SF("s2", 0);
		 rowdata.add(p76d);
		 String p76e = Fx.Get_SF("s4", 0);
		 rowdata.add(p76e);
		 double g_p76f = 5-Fx.Match_Condition(5, "s3");
		 rowdata.add((int)g_p76f+"场");
		 if(g_p76a >= 1.75 && g_p76a <= 2.00 && g_p76b > 0.6 && g_p76c >= 1 && p76d.equals("胜") && p76e.equals("胜") && g_p76f >= 3){
			 if(colc == "") colc = "PF6"; else colc += "、PF6";
		 }
		 
		 //项目77-3Z1
		 rowdata.add(df.format(Temp.WiliamD)+"");
		 rowdata.add(Temp.MacaoP == -10? "-":PanK[(int)((Temp.MacaoP + 4) / 0.25)]);

		 //项目78-3Z2
		 rowdata.add(Math.abs(Temp.zp - Temp.kp)+"");
		 rowdata.add(Temp.CrownP == -10? "-":PanK[(int)((Temp.CrownP + 4) / 0.25)]);

		 //项目79-3K1
		 rowdata.add(Temp.MacaoP == -10? "-":PanK[(int)((Temp.MacaoP + 4) / 0.25)]);
		 rowdata.add(df.format(Temp.WiliamD)+"");
		 
		 //项目80-YQXP7
		 double g_p80a = Fx.Get_Rank("kd");
		 rowdata.add(g_p80a == -1? "N/A":(int)g_p80a + "");
		 double g_p80b = Yz.Match_Condition(10, "np=-1&1.005<nlw");
		 rowdata.add((int)g_p80b + "个");
		 if(g_p80a == 1 && g_p80b > 2){
			 if(colc == "") colc = "YQXP7"; else colc += "、YQXP7";
		 }
		 
		 //项目81-ZP18
		 double g_p81a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 rowdata.add((int)g_p81a + "场");
		 double g_p81b = Fx.Match_Condition(2, "s5");
		 rowdata.add((int)g_p81b + "场");
		 double g_p81c = Yz.Company_state("澳门", "np");
		 rowdata.add(g_p81c == -10? "-":PanK[(int)((g_p81c + 4) / 0.25)]);
		 double g_p81d = Yz.Company_state("澳门", "nlw");
		 rowdata.add(g_p81d > 0? df.format(g_p81d):"-");
		 if(g_p81a == 2 && g_p81b == 2 && g_p81c <= 0 && g_p81c >= -0.5 && g_p81d > 0.975){
			 if(colc == "") colc = "ZP18"; else colc += "、ZP18";
		 }
		 
		 //项目82-FS5
		 double g_p82a = Fx.Series(10, "s5");;
		 rowdata.add((int)g_p82a+"场");
		 double g_p82b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p82b+"个");
		 double g_p82c = g_p58c;
		 rowdata.add((int)g_p82c+"个");
		 if(g_p82a > 3 && g_p82b > 4 && g_p82c >= 2){
			 if(colc == "") colc = "FS5"; else colc += "、FS5";
		 }
				 
		 //项目83-FS6
		 double g_p83a = g_p19a;
		 rowdata.add(g_p83a == F[0] && F[0] > 2? "Yes":"No");
		 double g_p83b = Yz.Match_Condition(15, "pp=-0.25&np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p83b + "个");
		 double g_p83c = Oz.Match_Condition(10, "3.30<=nl");
		 rowdata.add((int)g_p83c + "个");
		 if(g_p83a == F[0] && F[0] > 2 && g_p83b > 4 && g_p83c >= 8){
			 if(colc == "") colc = "FS6"; else colc += "、FS6";
		 }
		 	 
		 //项目85-ZP10
		 double g_p85a = g_p70e;
		 rowdata.add((int)g_p85a+"场");
		 double g_p85b = Fx.Match_Condition(3, "s5");
		 rowdata.add((int)g_p85b+"场");
		 double g_p85c = Oz.Match_Condition(10, "nw<pw&pd<nd&pl<nl");
		 rowdata.add((int)g_p85c+"个");
		 double g_p85d = Y[5];
		 rowdata.add((int)g_p85d+"个");
		 double g_p85e = Yz.Match_Condition(12, "1.005<nlw");
		 rowdata.add((int)g_p85e+"个");
		 if(g_p85a >= 2 && g_p85b >= 2 && g_p85c > 6 && g_p85d > 5 && g_p85e >= 2){
			 if(colc == "") colc = "ZP10"; else colc += "、ZP10";
		 }
		 
		 //项目86-ZP3K
		 double g_p86a = Yz.Company_state("Interwetten", "np");
		 rowdata.add(g_p86a == -10? "-":PanK[(int)((g_p86a + 4) / 0.25)]);
		 double g_p86b = Yz.Company_state("Interwetten", "nrw");
		 rowdata.add(g_p86b > 0? df.format(g_p86b) : "-");
		 double g_p86c = Oz.Company_state("Interwetten", "nd");
		 rowdata.add(g_p86c > 0? df.format(g_p86c) : "-");
		 double temp86a = Oz.Company_state("威廉希尔", "nd");
		 double temp86b = Oz.Company_state("威廉希尔", "pd");
		 double g_p86d = temp86a > 0 && temp86b > 0? temp86a - temp86b : 10;
		 g_p86d = (double)Math.round(g_p86d * 100) / 100;
		 rowdata.add(g_p86d != 10? df.format(g_p86d) : "-");
		 double g_p86e = Oz.Company_state("立博", "nd");
		 rowdata.add(g_p86e > 0? df.format(g_p86e) : "-");
		 if(g_p86a == 0 && g_p86b >= 1.3 && g_p86c >= 3.40 && g_p86d <= -0.2 && g_p86e >= 3.50){
			 if(colc == "") colc = "ZP3K"; else colc += "、ZP3K";
		 }
				 
		 //项目87-S4
		 double g_p87a = Dx.Match_Condition(10, "np<2.75&1.40<nd");
		 rowdata.add((int)g_p87a+"个");
		 double g_p87b = Fx.Match_Condition(4, "d4");
		 rowdata.add((int)g_p87b+"场");
		 double g_p87c = Fx.GP_Condition(4, "zg4=0&kg4=0");
		 rowdata.add((int)g_p87c+"场");
		 double g_p87d = Fx.Match_Condition(4, "d5");
		 rowdata.add((int)g_p87d+"场");
		 double g_p87e = Fx.GP_Condition(4, "zg5=0&kg5=0");
		 rowdata.add((int)g_p87e+"场");
		 double g_p87f = Fx.Match_Condition((int)F[0], "d1");
		 rowdata.add((int)g_p87f == F[0] && F[0] != 0? "Yes":"No");
		 if(g_p87a >= 2 && g_p87b >= 1 && g_p87c == 0 && g_p87d >= 1 && g_p87e == 0 && g_p87f == F[0] && F[0] != 0){
			 if(colc == "") colc = "S4"; else colc += "、S4";
		 }
				 
		 //项目88-S5
		 double g_p88a = Dx.Match_Condition(20, "np=0.5");
		 rowdata.add((int)g_p88a+"个");
		 if(g_p88a >= 1){
			 if(colc == "") colc = "S5"; else colc += "、S5";
		 }
				 
		 //项目89-KP2
		 double g_p89a = AveO10[2];
		 rowdata.add(df.format(g_p89a)); //>2.0);
		 double g_p89b = AveO10[0];
		 rowdata.add(df.format(g_p89b)); //1.60<=x<=2.1);
		 double g_p89c = g_p41c;
		 rowdata.add((int)g_p89c+"个");
		 double g_p89d = Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(df.format(g_p89d)); //3.49<=x<=3.8);
		 double g_p89e = F[0]-Fx.Match_Condition((int)F[0], "f1");
		 rowdata.add(g_p89e == F[0] && F[0] > 2? "Yes":"No");
		 double g_p89f = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p89f + "个");
		 String p89g = Fx.Get_SF("f4", 0);
		 rowdata.add(p89g);
		 double g_p89h = Oz.Match_Condition(10, "nw<pw&pd<nd");
		 rowdata.add((int)g_p89h + "个");
		 if(g_p89a > 2.00 && g_p89b >= 1.60 && g_p89b <= 2.10 && g_p89c >= 2 && g_p89d >= 3.49 && g_p89d <= 3.81 &&  g_p89e == F[0] && F[0] > 2 && 
				 g_p89f > 3 && p89g.equals("胜") && g_p89h > 4){
		 if(colc == "") colc = "KP2"; else colc += "、KP2";
		 }
		 
		 //项目90-PF21
		 double g_p90a = Fx.Series(10, "s4");
		 rowdata.add((int)g_p90a + "场");
		 double g_p90b = Fx.Series(10, "f5");
		 rowdata.add((int)g_p90b + "场");
		 double g_p90c = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p90c + "个");
		 if(g_p90a >= 2 && g_p90b >= 2 && g_p90c > 2){
			 if(colc == "") colc = "PF21"; else colc += "、PF21";
		 }
				 
		 //项目91-ZP14
		 String p91a = Fx.Get_SF("f4", 0);
		 rowdata.add(p91a);
		 String p91b = Fx.Get_SF("f5", 0);
		 rowdata.add(p91b);
		 double g_p91c = AveO10[0];
		 rowdata.add(df.format(g_p91c) + "");
		 double g_p91d = AveO10[2];
		 rowdata.add(df.format(g_p91d) + "");
		 double g_p91e = Oz.Match_Condition(10, "3.50<nd");
		 rowdata.add((int)g_p91e + "个");
		 double g_p91f = Yz.Match_Condition(10, "pp=0.25&np=0.5");
		 rowdata.add((int)g_p91f + "个");
		 if(p91a.equals("平") && p91b.equals("平") && g_p91c > 2.00 && g_p91d >= 1.60 && g_p91d <= 2.10 && g_p91e > 2 && g_p91f > 4){
			 if(colc == "") colc = "ZP14"; else colc += "、ZP14";
		 }
		 
		 //项目92-ZP21
		 double g_p92a = Fx.Series(10, "f4");
		 rowdata.add((int)g_p92a + "场");
		 double g_p92b = Fx.Series(10, "s5");
		 rowdata.add((int)g_p92b + "场");
		 double g_p92c = Yz.Match_Condition(10, "np<-0.25&-10<np");
		 rowdata.add((int)g_p92c + "个");
		 if(g_p92a >= 2 && g_p92b >= 2 && g_p92c > 4){
			 if(colc == "") colc = "ZP21"; else colc += "、ZP21";
		 }
		 
		 //项目93-ZP22
		 double g_p93a = Fx.Series(10, "f4");
		 rowdata.add((int)g_p93a + "场");
		 double g_p93b = Fx.Series(10, "s5");
		 rowdata.add((int)g_p93b + "场");
		 double g_p93c = Yz.Match_Condition(10, "np=-0.25&1.005<nrw");
		 rowdata.add((int)g_p93c + "个");
		 if(g_p93a >= 2 && g_p93b >= 2 && g_p93c > 4){
			 if(colc == "") colc = "ZP22"; else colc += "、ZP22";
		 }
		 
		 //项目94-ZP23
		 double g_p94a = Fx.Series(10, "s4");
		 rowdata.add((int)g_p94a + "场");
		 double g_p94b = Fx.Series(10, "f5");
		 rowdata.add((int)g_p94b + "场");
		 double g_p94c = Yz.Match_Condition(10, "0.25<np&np<1&1.005<nrw");
		 rowdata.add((int)g_p94c + "个");
		 if(g_p94a >= 2 && g_p94b >= 2 && g_p94c > 2){
			 if(colc == "") colc = "ZP23"; else colc += "、ZP23";
		 }
		 
		 //项目96-SF6
		 double g_p96a = Fx.Match_Condition(4, "p4") + Fx.Match_Condition(4, "f4");
		 rowdata.add((int)g_p96a + "场");
		 double g_p96b = Fx.Match_Condition(4, "p5") + Fx.Match_Condition(4, "s5");
		 rowdata.add((int)g_p96b + "场");
		 double g_p96c = Fx.Match_Condition(4, "s5");
		 rowdata.add((int)g_p96c + "场");
		 double g_p96d = Yz.Match_Condition(10, "-0.5<np&np<0.25&1.005<nrw");
		 rowdata.add((int)g_p96d + "个");
		 if(g_p96a == 4 && g_p96b == 4 && g_p96c > 1 && g_p96d > 4){
			 if(colc == "") colc = "SF6"; else colc += "、SF6";
		 }
						 
		 //项目97-ZP1
		 double g_p97a = Yz.Match_Condition(10, "np<pp&1.005<=plw&1.005<=nlw");
		 rowdata.add((int)g_p97a + "个");
		 double g_p97b = 4 - Fx.Match_Condition(4, "s5");
		 rowdata.add((int)g_p97b + "场");
		 double g_p97c = Fx.Match_Condition(4, "f5");
		 rowdata.add((int)g_p97c + "场");
		 double g_p97d = Fx.Match_Condition(4, "s4");
		 rowdata.add((int)g_p97d + "场");
		 if(g_p97a >= 2 && g_p97b == 4 && g_p97c >= 2 && g_p97d >= 2){
			 if(colc == "") colc = "ZP1"; else colc += "、ZP1";
		 }
		 
		 //项目99-BL
		 double temp99a = Oz.Company_state("威廉希尔", "nd");
		 double temp99b = Oz.Company_state("立博", "nd");
		 double g_p99a = temp99a > 0 && temp99b > 0? temp99a - temp99b : -10;
		 g_p99a = (double)Math.round(g_p99a * 100) / 100;
		 rowdata.add(g_p99a != -10? g_p99a + "" : "-");
		 double g_p99b = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p99b + "个");
		 if(g_p99a > 0.1 && g_p99b >= 1){
			 if(colc == "") colc = "BL"; else colc += "、BL";
		 }
			
		 //项目100-ZP3K1
		 double g_p100a = Yz.Company_state("Interwetten", "np");
		 rowdata.add(g_p100a == -10? "-":PanK[(int)((g_p100a + 4) / 0.25)]);
		 double g_p100b = Yz.Company_state("Interwetten", "nrw");
		 rowdata.add(g_p100b > 0? df.format(g_p100b) : "-");
		 double g_p100c = Oz.Company_state("Interwetten", "nd");
		 rowdata.add(g_p100c > 0? df.format(g_p100c) : "-");
		 double temp100a = Oz.Company_state("威廉希尔", "nd");
		 double temp100b = Oz.Company_state("立博", "nd");
		 double g_p100d = temp100a > 0 && temp100b > 0? temp100a - temp100b : -10;
		 g_p100d = (double)Math.round(g_p100d * 100) / 100;
		 rowdata.add(g_p100d != -10? df.format(g_p100d) : "-");
		 double g_p100e = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p100e + "个");
		 if(g_p100a == 0 && g_p100b >= 1.3 && g_p100c >= 3.40 && g_p100d > 0 && g_p100e > 2){
			 if(colc == "") colc = "ZP3K1"; else colc += "、ZP3K1";
		 }
		 
		 //项目101-FS1
		 double g_p101a = 2 - Fx.Match_Condition(2, "s4");
		 rowdata.add((int)g_p101a + "场");
		 double g_p101b = Fx.Match_Condition(2, "s5");
		 rowdata.add((int)g_p101b + "场");
		 double g_p101c = Yz.Match_Condition(10, "pp=-0.25&np=-0.5");
		 rowdata.add((int)g_p101c + "个");
		 double g_p101d = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p101d + "个");
		 double g_p101e = Oz.Match_Condition(10, "3.50<nd");
		 rowdata.add((int)g_p101e + "个");
		 if(g_p101a == 2 && g_p101b == 2 && g_p101c > 3 && g_p101d > 2 && g_p101e > 4){
			 if(colc == "") colc = "FS1"; else colc += "、FS1";
		 }
			
		 //项目102-2Z1
		 rowdata.add(Temp.zp > 0? Temp.zp + "" : "-");
		 rowdata.add(Temp.kp > 0? Temp.kp + "" : "-");
		 rowdata.add(Temp.zzj + "");
		 rowdata.add(Temp.Bet365P == -10? "-":PanK[(int)((Temp.Bet365P + 4) / 0.25)]);
		 rowdata.add(Temp.winG + "个");
		 
		 //项目103-2P2
		 rowdata.add(Temp.Bet365P == -10? "-":PanK[(int)((Temp.Bet365P + 4) / 0.25)]);
		 rowdata.add(Temp.rlw1005 + "个");
		 
		 //项目104-ZP18ZP2K
		 double g_p104a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 rowdata.add((int)g_p104a + "场");
		 double g_p104b = Fx.Match_Condition(2, "s4");
		 rowdata.add((int)g_p104b + "场");
		 double g_p104c = Yz.Company_state("澳门", "np");
		 rowdata.add(g_p104c == -10? "-":PanK[(int)((g_p104c + 4) / 0.25)]);
		 double g_p104d = Yz.Company_state("澳门", "nlw");
		 rowdata.add(g_p104d > 0? df.format(g_p104d):"-");
		 double g_p104e = Fx.Match_Condition(3, "s2") + Fx.Match_Condition(3, "p2");
		 rowdata.add((int)g_p104e + "场");
		 double g_p104f = Fx.Match_Condition(3, "s2");
		 rowdata.add((int)g_p104f + "场");
		 double g_p104g = Yz.Match_Condition(10, "nrw<0.8");
		 rowdata.add((int)g_p104g + "个");
		 if(g_p104a == 2 && g_p104b == 2 && g_p104c <= 0 && g_p104c >= -0.5 && g_p104d > 0.975 && g_p104e == 3 && g_p104f > 1 && g_p104g >= 1){
			 if(colc == "") colc = "ZP18ZP2K"; else colc += "、ZP18ZP2K";
		 }
		 
		 //项目105-ZP26
		 double g_p105a = Fx.Series(10, "s5");
		 rowdata.add((int)g_p105a + "场");
		 double g_p105b = Fx.Series(10, "f4");
		 rowdata.add((int)g_p105b + "场");
		 double g_p105c = Yz.Match_Condition(10, "np=0.5&1.005<nrw");
		 rowdata.add((int)g_p105c + "个");
		 double g_p105d = Oz.Match_Condition(10, "nl<pl");
		 rowdata.add((int)g_p105d + "个");
		 if(g_p105a >= 2 && g_p105b >= 2 && g_p105c >= 4 && g_p105d > 5){
			 if(colc == "") colc = "ZP26"; else colc += "、ZP26";
		 }
		 
		 //项目106-P6
		 String p106a = Fx.Get_SF("s4", 0);
		 rowdata.add(p106a);
		 String p106b = Fx.Get_SF("s5", 0);
		 rowdata.add(p106b);
		 double g_p106c = O[15];
		 rowdata.add((int)g_p106c + "个");
		 double g_p106d = O[16];
		 rowdata.add((int)g_p106d + "个");
		 double g_p106e = Oz.Match_Condition(10, "nd<=3.30");
		 rowdata.add((int)g_p106e + "个");
		 double g_p106f = Dx.Match_Condition(10, "1.00<nd");
		 rowdata.add((int)g_p106f + "个");
		 if(p106a.equals("负") && p106b.equals("胜") && g_p106c > 2 && g_p106d > 5 && g_p106e > 6 && g_p106f > 5){
			 if(colc == "") colc = "P6"; else colc += "、P6";
		 }
				 
		 //项目114-QDBS7
		 double g_p114a = Fx.Series(10, "s4");
		 rowdata.add((int)g_p114a + "场");
		 double g_p114b = Fx.Series(10, "f5");
		 rowdata.add((int)g_p114b + "场");
		 double g_p114c = Fx.Match_Condition((int)F[0], "s1");
		 rowdata.add((int)g_p114c + "场");
		 double g_p114d = Yz.Match_Condition(10, "np=-1&1.005<nlw");
		 rowdata.add((int)g_p114d + "个");
		 double g_p114e = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p114e + "个");
		 if(g_p114a >= 4 && g_p114b >= 3 && g_p114c > 2 && g_p114d >= 2 && g_p114e > 6){
			 if(colc == "") colc = "QDBS7"; else colc += "、QDBS7";
		 }
				 
		 //项目115-YK1
		 double g_p115a = Yz.Match_Condition(10, "pp<np&nrw<prw");
		 rowdata.add((int)g_p115a + "个");
		 double g_p115b = Yz.Match_Condition(15, "1.005<nrw");
		 rowdata.add((int)g_p115b + "个");
		 String p115c = Fx.Get_SF("s5", 0);
		 rowdata.add(p115c);
		 double g_p115d = Oz.Match_Condition(10, "nl<pl");
		 rowdata.add((int)g_p115d + "个");
		 if(g_p115a > 5 && g_p115b >= 2 && p115c.equals("胜") && g_p115d > 6){
			 if(colc == "") colc = "YK1"; else colc += "、YK1";
		 }
		 	 
		 //项目117-Z2
		 double g_p117a = Fx.Match_Condition((int)F[0], "s1");
		 rowdata.add((int)g_p117a + "场");
		 double g_p117b = Fx.Match_Condition(5, "s2");
		 rowdata.add((int)g_p117b + "场");
		 double g_p117c = Oz.Match_Condition(10, "pw<nw&nd<pd&nl<pl");
		 rowdata.add((int)g_p117c + "个");
		 double g_p117d = Oz.Match_Condition(10, "3.60<=nd");
		 rowdata.add((int)g_p117d + "个");
		 double g_p117e = Yz.Match_Condition(10, "pp<np&1.005<nlw&1.005<plw");
		 rowdata.add((int)g_p117e + "个");
		 String p117f = Fx.Get_SF("s4", 0);
		 rowdata.add(p117f);
		 String p117g = Fx.Get_SF("s1", 0);
		 rowdata.add(p117g);
		 if(g_p117a > 3 && g_p117b > 3 && g_p117c > 5 && g_p117d > 2 && g_p117e >= 1 && (p117f.equals("负")|p117g.equals("负"))){
			 if(colc == "") colc = "Z2"; else colc += "、Z2";
		 }
				 		 
		 //项目118-Z3
		 double g_p118a = w;
		 rowdata.add(df.format(g_p118a) + "");
		 double g_p118b = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p118b + "个");
		 double g_p118c = Oz.Match_Condition(10, "nd=nw");
		 rowdata.add((int)g_p118c + "个");
		 if(g_p118a > 3.60 && g_p118b > 5 && g_p118c >= 1 && leixing == 2){
		 	 if(colc == "") colc = "Z3"; else colc += "、Z3";
		 }
		 
		 //项目124-K7
		 double g_p124a = l;
		 rowdata.add(df.format(g_p124a) + "");
		 double g_p124b = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p124b + "个");
		 double g_p124c = Oz.Match_Condition(10, "nd=nl");
		 rowdata.add((int)g_p124c + "个");
		 if(g_p124a > 3.60 && g_p124b > 5 && g_p124c >= 1 && leixing == 2){
			 if(colc == "") colc = "K7"; else colc += "、K7";
		 }
			
		 //项目126-ZP19
		 double g_p126a = Yz.Match_Condition(10, "pp=-0.75&np<=-1");
		 rowdata.add((int)g_p126a + "个");
		 double g_p126b = Oz.Match_Condition(10, "nl<=5.0");
		 rowdata.add((int)g_p126b + "个");
		 double g_p126c = AveO10[1];
		 rowdata.add(df.format(g_p126c) + "");
		 String p126d = Fx.Get_SF("s4", 0);
		 rowdata.add(p126d);
		 if(g_p126a > 4 && g_p126b >= 3 && g_p126c > 4.0 && p126d.equals("胜")){
			 if(colc == "") colc = "ZP19"; else colc += "、ZP19";
		 }
				 
		 //项目127-Z11
		 double g_p127a = w;
		 rowdata.add(df.format(g_p127a) + "");
		 double g_p127b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p127b + "个");
		 double g_p127c = AveO10[2];
		 rowdata.add(df.format(g_p127c) + "");
		 if(g_p127a < 1.80 && g_p127a > 0 && g_p127b >= 2 && g_p127c < 3.50 && leixing == 2){
			 if(colc == "") colc = "Z11"; else colc += "、Z11";
		 }
		
		 //项目128-Z8
		 double g_p128a = Yz.Match_Condition(10, "pp=-0.75&np=-1");
		 rowdata.add((int)g_p128a + "个");
		 double g_p128b = Oz.Match_Condition(10, "nl<=5.0");
		 rowdata.add((int)g_p128b + "个");
		 String p128c = Fx.Get_SF("s5", 0);
		 rowdata.add(p128c);
		 String p128d = Fx.Get_SF("s4", 0);
		 rowdata.add(p128d);
		 String p128e = Fx.Get_SF("s1", 0);
		 rowdata.add(p128e);
		 if(g_p128a > 4 && g_p128b >= 3 && p128c.equals("负") && p128d.equals("胜") &&  p128e.equals("胜")){
			 if(colc == "") colc = "Z8"; else colc += "、Z8";
		 }
				 
		 //项目130-F2
		 double g_p130a = 6 - Fx.Match_Condition(6, "f2");
		 rowdata.add((int)g_p130a + "场");
		 String p130b = Fx.Get_SF("s1", 0);
		 rowdata.add(p130b);
		 double g_p130c = Yz.Match_Condition(10, "np=-0.25&1.005<nlw");
		 rowdata.add((int)g_p130c + "个");
		 double g_p130d = Oz.Match_Condition(10, "3.40<nd");
		 rowdata.add((int)g_p130d + "个");
		 if(g_p130a == 6 && (p130b.equals("胜")|p130b.equals("平")) && g_p130c >= 4 && g_p130d >= 5){
			 if(colc == "") colc = "F2"; else colc += "、F2";
		 }
				 
		 //项目133-F4
		 double g_p133a = Fx.Match_Condition(6, "s4");
		 rowdata.add((int)g_p133a + "场");
		 double g_p133b = Fx.Match_Condition(6, "s5");
		 rowdata.add((int)g_p133b + "场");
		 double g_p133c = Fx.Match_Condition(3, "p1") + Fx.Match_Condition(3, "f1");
		 rowdata.add((int)g_p133c + "场");
		 double g_p133d = Oz.Company_state("威廉希尔", "nw");
		 rowdata.add(df.format(g_p133d));
		 double g_p133e = Oz.Company_state("威廉希尔", "nd");
		 rowdata.add(df.format(g_p133e));
		 double g_p133f = Oz.Company_state("Ladbrokes (立博)", "nw");
		 rowdata.add(df.format(g_p133f));
		 double g_p133g = Oz.Company_state("Ladbrokes (立博)", "nd");
		 rowdata.add(df.format(g_p133g));
		 if(g_p133a >= 4 && g_p133b >= 4 && g_p133c > 1 && ((g_p133d>=1.80&g_p133e>3.60)|(g_p133d>=1.75&g_p133e>3.80)) &&  ((g_p133f>=1.80&g_p133g>3.60)|(g_p133f>=1.75&g_p133g>3.80))){
			 if(colc == "") colc = "F4"; else colc += "、F4";
		 }
				 
		 //项目137-F5
		 double g_p137a = 5 - Fx.Match_Condition(5, "s4");
		 rowdata.add((int)g_p137a + "场");
		 double g_p137b = 5 - Fx.Match_Condition(5, "s5");
		 rowdata.add((int)g_p137b + "场");
		 double g_p137c = Oz.Match_Condition(10, "pd=pl&pd>=3.30&pl>=3.30");
		 rowdata.add((int)g_p137c + "个");
		 double g_p137d = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p137d + "个");
		 if(g_p137a >= 4 && g_p137b >= 4 && g_p137c >= 2 && g_p137d > 4){
			 if(colc == "") colc = "F5"; else colc += "、F5";
		 }
				 
		 //项目138-PF13
		 double g_p138a = 5 - Fx.Match_Condition(5, "s4");
		 rowdata.add((int)g_p138a + "场");
		 double g_p138b = 5 - Fx.Match_Condition(5, "f5");
		 rowdata.add((int)g_p138b + "场");
		 double g_p138c = Fx.Match_Condition(5, "s5");
		 rowdata.add((int)g_p138c + "场");
		 double g_p138d = Oz.Match_Condition(10, "nd=nw&3.40<=nd&3.40<=nw");
		 rowdata.add((int)g_p138d + "个");
		 double g_p138e = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p138e + "个");
		 if(g_p138a >= 4 && g_p138b >= 4 && g_p138c >= 3 && g_p138d >= 1 && g_p138e > 4){
			 if(colc == "") colc = "PF13"; else colc += "、PF13";
		 }
				 
		 //项目139-KDBS2
		 double g_p139a = Fx.Series(10, "s5");
		 rowdata.add((int)g_p139a + "场");
		 double g_p139b = Oz.Match_Condition(10, "nl<pl");
		 rowdata.add((int)g_p139b + "个");
		 double g_p139c = 3 - Fx.Match_Condition(3, "s4");
		 rowdata.add(df.format(g_p139c) + "");
		 double g_p139d = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p139d + "个");
		 double g_p139e = Yz.Match_Condition(10, "prw<0.90");
		 rowdata.add((int)g_p139e + "个");
		 if(g_p139a >= 2 && g_p139b > 5 && g_p139c == 3 && g_p139c > 0 && g_p139d > 3 && g_p139e > 7 && leixing == 2){
			 if(colc == "") colc = "KDBS2"; else colc += "、KDBS2";
		 }
				 
		 //项目141-Z7
		 String p141a = Fx.Get_SF("s1", 0);
		 rowdata.add(p141a);
		 String p141b = Fx.Get_SF("s5", 0);
		 rowdata.add(p141b);
		 double g_p141c = Oz.Match_Condition(10, "pw<pl");
		 rowdata.add((int)g_p141c + "个");
		 double g_p141d = Oz.Match_Condition(10, "nl<nw");
		 rowdata.add((int)g_p141d + "个");
		 double g_p141e = Yz.Match_Condition(10, "1.005<plw");
		 rowdata.add((int)g_p141e + "个");
		 double g_p141f = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p141f + "个");
		 double g_p141g = Yz.Match_Condition(20, "np=0.25&1.005<nrw");
		 rowdata.add((int)g_p141g + "个");
		 double g_p141h = Oz.Match_Condition(10, "3.40<=nd");
		 rowdata.add((int)g_p141h + "个");
		 if((p141a.equals("平")|p141a.equals("负")) && p141b.equals("胜") && g_p141c > 8 && g_p141d > 6 && g_p141e > 5 && g_p141f > 5 && g_p141g >= 2 && g_p141h >= 3){
			 if(colc == "") colc = "Z7"; else colc += "、Z7";
		 }
				 
	     //项目142-ZS2
		 double g_p142a = w;
		 rowdata.add(df.format(g_p142a) + "");
		 double g_p142b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p142b + "个");
		 String p142c = Fx.Get_SF("s5", 0);
		 rowdata.add(p142c);
		 String p142d = Fx.Get_SF("s4", 0);
		 rowdata.add(p142d);
		 if(g_p142a < 1.80 && g_p142a > 0 && g_p142b >= 5 && (p142c.equals("平")|p142c.equals("负"))& p142d.equals("胜") && leixing == 2){
			 if(colc == "") colc = "ZS2"; else colc += "、ZS2";
		 }
				 
	     //项目144-ZP15
		 double g_p144a = l;
		 rowdata.add(df.format(g_p144a) + "");
		 double g_p144b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nrw");
		 rowdata.add((int)g_p144b + "个");
		 double g_p144c = AveO10[0]; 
		 rowdata.add(df.format(g_p144c) + "");
		 if(g_p144a < 1.80 && g_p144a > 0 && g_p144b >= 2 && g_p144c < 3.80 && leixing == 2){
			 if(colc == "") colc = "ZP15"; else colc += "、ZP15";
		 }
		 
		 //项目145-Z
		 double g_p145a = Fx.Series(10, "s2");
		 rowdata.add((int)g_p145a + "场");
		 String p145b = Fx.Get_SF("s1", 0);
		 rowdata.add(p145b);
		 String p145c = Fx.Get_SF("s5", 0);
		 rowdata.add(p145c);
		 double g_p145d = Oz.Match_Condition(10, "pw<nw");
		 rowdata.add((int)g_p145d + "个");
		 double g_p145e = O[14];
		 rowdata.add((int)g_p145e + "个");
		 double g_p145f = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p145f + "个");
		 if(g_p145a >= 3 && (p145b.equals("胜")|p145b.equals("平")) && (p145c.equals("平")|p145c.equals("负")) && g_p145d > 5 && g_p145e >= 4 && g_p145f >= 2){
			 if(colc == "") colc = "Z"; else colc += "、Z";
		 }
				 
		 //项目147-P3
		 double g_p147a = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p147a + "个");
		 double g_p147b = Oz.Match_Condition(10, "nw=nl");
		 rowdata.add((int)g_p147b + "个");
		 double g_p147c = Oz.Match_Condition(10, "pl<pw&nw<nl");
		 rowdata.add((int)g_p147c + "个");
		 double g_p147d = Oz.Match_Condition(10, "nd<3.30");
		 rowdata.add((int)g_p147d + "个");
		 if(g_p147a > 3 && g_p147b >= 1 && g_p147c > 6 && g_p147d > 8){
			 if(colc == "") colc = "P3"; else colc += "、P3";
		 }
				 
		 //项目149-ZP16
		 double g_p149a = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p149a + "个");
		 double g_p149b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 rowdata.add((int)g_p149b + "个");
		 double g_p149c = Oz.Match_Condition(10, "nd<3.30");
		 rowdata.add((int)g_p149c + "个");
		 if(g_p149a > 6 && g_p149b > 4 && g_p149c > 8){
			 if(colc == "") colc = "ZP16"; else colc += "、ZP16";
		 }
				 
		 //项目151-BF
		 double g_p151a = Dx.Match_Condition(10, "3.00<nd or 3.00<nx");
		 rowdata.add((int)g_p151a + "个");
		 if(g_p151a > 0){
			 if(colc == "") colc = "BF"; else colc += "、BF";
		 }
		 
		 //项目153-ZP11
		 double g_p153a = Fx.Match_Condition(10, "s3");
		 rowdata.add((int)g_p153a + "场");
		 double g_p153b = Oz.Match_Condition(10, "pl<pd");
		 rowdata.add((int)g_p153b + "个");
		 double g_p153c = Oz.Match_Condition(10, "nd<nl");
		 rowdata.add((int)g_p153c + "个");
		 double g_p153d = Oz.Match_Condition(10, "3.40<=nd");
		 rowdata.add((int)g_p153d + "个");
		 double g_p153e = Yz.Match_Condition(10, "nlw<1.005");
		 rowdata.add((int)g_p153e + "个");
		 if(g_p153a <= 1 && g_p153b > 8 && g_p153c > 6 && g_p153d > 4 && g_p153e > 8){
			 if(colc == "") colc = "ZP11"; else colc += "、ZP11";
		 }
				 
		 //项目154-QDXP5
		 double g_p154a = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p154a + "个");
		 double g_p154b = Dx.Match_Condition(10, "pp<np&nd<pd");
		 rowdata.add((int)g_p154b + "个");
		 if(g_p154a > 5 && g_p154b > 2){
			 if(colc == "") colc = "QDXP5"; else colc += "、QDXP5";
		 }
		 
		 //项目160-Z10
		 double g_p160a = 10 - Fx.Match_Condition(10, "f3");
		 rowdata.add((int)g_p160a + "场");
		 String p160b = Fx.Get_SF("s4", 0);
		 rowdata.add(p160b);
		 String p160c = Fx.Get_SF("s1", 0);
		 rowdata.add(p160c);
		 double g_p160d = Oz.Match_Condition(10, "nl=nd&3.20<nd&3.20<nl");
		 rowdata.add((int)g_p160d + "个");
		 if(g_p160a == 10 && p160b.equals("负") && p160c.equals("负") && g_p160d >= 1){
			 if(colc == "") colc = "Z10"; else colc += "、Z10";
		 }
		 
		 //项目163-PF7
		 double g_p163a = 10 - Fx.Match_Condition(10, "s3");
		 rowdata.add((int)g_p163a + "场");
		 double g_p163b = Oz.Match_Condition(10, "nl<nd");
		 rowdata.add((int)g_p163b + "个");
		 double g_p163c = Oz.Match_Condition(10, "nw<pw");
		 rowdata.add((int)g_p163c + "个");
		 double g_p163d = AveO10[0];
		 rowdata.add(df.format(g_p163d));
		 double g_p163e = Oz.Match_Condition(10, "3.50<nd");
		 rowdata.add((int)g_p163e + "个");
		 if(g_p163a == 10 && g_p163b >= 4 && g_p163c > 8 && g_p163d > 1.80 && g_p163e > 5){
			 if(colc == "") colc = "PF7"; else colc += "、PF7";
		 }
		 
		 //项目167-SF4
		 double g_p167a = Fx.Match_Condition(2, "f1");
		 rowdata.add((int)g_p167a + "场");
		 double g_p167b = Fx.Match_Condition(2, "f4");
		 rowdata.add((int)g_p167b + "场");
		 double g_p167c = 2 - Fx.Match_Condition(2, "s5");
		 rowdata.add((int)g_p167c + "场");
		 double g_p167d = 10 - Fx.Match_Condition(10, "f2");
		 rowdata.add((int)g_p167d + "场");
		 double g_p167e = Oz.Match_Condition(10, "nl<pl");
		 rowdata.add((int)g_p167e + "个");
		 double g_p167f = Oz.Match_Condition(10, "nd<=3.20");
		 rowdata.add((int)g_p167f + "个");
		 double g_p167g = Yz.Match_Condition(10, "1.005<nrw");
		 rowdata.add((int)g_p167g + "个");
		 if(g_p167a == 2 && g_p167b == 2 && g_p167c == 2 && g_p167d > 6 && g_p167e > 7 && g_p167f > 8 && g_p167g < 3){
			 if(colc == "") colc = "SF4"; else colc += "、SF4";
		 }
		 
		 //项目169-QDBS9
		 double g_p169a = Yz.Match_Condition(10, "pp=-0.75&np=-1");
		 rowdata.add((int)g_p169a + "个");
		 double g_p169b = Oz.Match_Condition(10, "nl<=5.0");
		 rowdata.add((int)g_p169b + "个");
		 double g_p169c = AveO10[1];
		 rowdata.add(df.format(g_p169c));
		 String p169d = Fx.Get_SF("s4", 0);
		 rowdata.add(p169d);
		 if(g_p169a > 4 && g_p169b >= 3 && g_p169c > 4.0 && p169d.equals("负")){
			 if(colc == "") colc = "QDBS9"; else colc += "、QDBS9";
		 }
		 
		 //项目179-F10
		 double g_p179a = Fx.Match_Condition(2, "s2");
		 rowdata.add((int)g_p179a + "场");
		 double g_p179b = Fx.Match_Condition(2, "f3");
		 rowdata.add((int)g_p179b + "场");
		 double g_p179c = 2 - Fx.Match_Condition(2, "f1");
		 rowdata.add((int)g_p179c + "场");
		 double g_p179d = Yz.Match_Condition(10, "pp=-0.5&1.005<plw");
		 rowdata.add((int)g_p179d + "个");
		 double g_p179e = Yz.Match_Condition(10, "np=-0.25&1.005<nlw");
		 rowdata.add((int)g_p179e + "个");
		 double g_p179f = Oz.Match_Condition(10, "3.40<=nd");
		 rowdata.add((int)g_p179f + "个");
		 double g_p179g = Oz.Match_Condition(10, "nd=nl");
		 rowdata.add((int)g_p179g + "个");
		 if(g_p179a == 2 && g_p179b == 2 && g_p179c == 2 && g_p179d >= 3 && g_p179e >= 4 &
				 g_p179f >= 2 && g_p179g > 1){
			 if(colc == "") colc = "F10"; else colc += "、F10";
		 }
		 
		 //项目188-F7
		 double g_p188a = Fx.Series((int)F[0], "f1");
		 rowdata.add((int)g_p188a + "场");
		 double g_p188b = Oz.Company_state("威廉希尔", "pw") == 0? 
				 0:100*(Oz.Company_state("威廉希尔", "pw")-Oz.Company_state("威廉希尔", "nw"))/Oz.Company_state("威廉希尔", "pw");
		 rowdata.add(df.format(g_p188b)+ "%");
		 double g_p188c = Oz.Company_state("威廉希尔", "pl") == 0? 
				 0:100*(Oz.Company_state("威廉希尔", "nl")-Oz.Company_state("威廉希尔", "pl"))/Oz.Company_state("威廉希尔", "pl");
		 rowdata.add(df.format(g_p188c)+ "%");
		 double g_p188d = Oz.Company_state("威廉希尔", "pd") == 0? 
				 0:100*(Oz.Company_state("威廉希尔", "nd")-Oz.Company_state("威廉希尔", "pd"))/Oz.Company_state("威廉希尔", "pd");
		 rowdata.add(df.format(g_p188d)+ "%");
		 double g_p188e = Yz.Match_Condition(10, "prw<0.95");
		 rowdata.add((int)g_p188e + "个");
		 double g_p188f = Yz.Match_Condition(10, "nrw<0.95");
		 rowdata.add((int)g_p188f + "个");
		 if(g_p188a >= 2 && g_p188b > 20 && g_p188c > 20 && g_p188d > 0 && g_p188e > 8 && g_p188f > 8){
			 if(colc == "") colc = "F7"; else colc += "、F7";
		 }
		 
		 //项目189-D3
		 double g_p189a = Dx.Match_Condition(10, "pp=3.5&np=3.0&1.05<nd");
		 rowdata.add((int)g_p189a + "个");
		 double g_p189b = Yz.Match_Condition(10, "1.005<nlw");
		 rowdata.add((int)g_p189b + "个");
		 if(g_p189a >= 2 && g_p189b > 5){
			 if(colc == "") colc = "D3"; else colc += "、D3";
		 }
		 
		 if(Q.size() == 5)
			 Q.remove(0);
		 Q.add(Temp);
		 if(Common_Use.Satisfy(Q, "3Z1"))
			 disp3("3Z1", count - 1);
		 if(Common_Use.Satisfy(Q, "3Z2"))
			 disp3("3Z2", count - 1);
		 if(Common_Use.Satisfy(Q, "3K1"))
			 disp3("3K1", count - 1);
		 if(Common_Use.Satisfy(Q, "2P1"))
			 disp3("2P1", count - 1);
		 if(Common_Use.Satisfy(Q, "2Z1"))
			 disp3("2Z1", count - 1);
		 if(Common_Use.Satisfy(Q, "2P2"))
			 disp3("2P2", count - 1);
		 
  		 if(colc.equals(""))
  			 colc = "-";
		 rowdata.add(1, colc);
  		 return rowdata;
	}
	
	public void disp3(String str, int row)
	{
		int temp = str.charAt(0) - '0';
		for(int i = 1; i <= temp; i++)
		{
			if(table.getValueAt(row - i, 1).equals("-"))
				table.setValueAt(str, row - i, 1);
			else
				table.setValueAt(table.getValueAt(row - i, 1) + "、" + str, row - i, 1);
		}
	}
	
	public String[] set_pre()
	{
		String date = "";
		String s = "";
		String html = "";
		if(leixing == 1)
		{
			s = "完整版";
			html = "http://live.500.com/2h1.php";
		}
		else if(leixing == 2)
		{
			do{
			date = choice.getSelectedItem();
		    }while(date == null);
			date = date.substring(7, date.length()-7);
			s = "单场第"+ date + "期";
			html = "http://live.500.com/zqdc.php?e=" + date;
		}
		else if(leixing == 3)
		{
			do{
			date = choice.getSelectedItem();
			}while(date == null);
			date = date.substring(5, date.length()-5);
			s = "竞彩    "+ date;
			html = "http://live.500.com/?e="+date;
		}
		else if(leixing == 4)
		{
			do{
			date = choice.getSelectedItem();
			}while(date == null);
			date = date.substring(7, date.length()-8);
			s = "足彩第"+ date+"期";
			html = "http://live.500.com/zucai.php?e="+date;
		}
		else if(leixing == 5 | leixing == 6)
		{
			do{
			date = choice.getSelectedItem();
		    }while(date == null);
			date = date.substring(5, date.length()-5);
			s =leixing == 5? "完场    "+ date:"未来比赛    "+ date;
			html = "http://live.500.com/wanchang.php?e="+date;
		}
		String[] result = {date,s,html};
		return result;
	}
	public static String sort(double w,double d,double l)
	{
		double[] pre = {w,d,l};
		double[] now = {w,d,l};
		Arrays.sort(now);
		String re = "";
		for(int i = 0 ; i < 3 ; i++)
			for(int j = 0 ; j < 3 ; j++)
			{
				if(pre[i] == now[j])
				{
					if(j == 0 && re.indexOf("A") == -1)
						re += "A";
					if(j == 1 && re.indexOf("B") == -1)
						re += "B";
					if(j == 2 && re.indexOf("C") == -1)
						re += "C";
				}
			}
		return re;
	}
	public static double[] match_SP(String id,String list)
	{
		int begin = list.indexOf(id);
		if(begin == -1)
		{
			double[] res = {0,0,0};
			return res;
		}
		list = list.substring(begin);
		if(list.indexOf("rqsp") == -1){
			double[] res = {0,0,0};
			return res;
		}
		begin = list.indexOf("rqsp") + 7;
		list = list.substring(begin);
		int end = list.indexOf("]");
		list = list.substring(0,end);
		String st[] = list.split(",");
		double[] res = new double[3];
		res[0] = Double.parseDouble(st[0]);
		res[1] = Double.parseDouble(st[1]);
		res[2] = Double.parseDouble(st[2]);
		return res;
	}
	//数据初始化
	public void init() throws IOException, InterruptedException
	{
		try {
			 Q.clear();
			 Q.add(new InfoStore("-","-","-","-","-","-","-","-"));
			 if(start_over) 
	   			   return;
			 String[] r = set_pre();
			 textarea.setText("足彩赔率分析系统\n");
			 textarea.append("连接中...\n");
			 textarea.append(r[1] + "\n");
			 textarea.setCaretPosition(textarea.getText().length());
			 
			 CloseableHttpClient httpclient = HttpClients.createDefault();
             HttpGet httpget = new HttpGet(r[2]);
             HttpResponse response = httpclient.execute(httpget);
             HttpEntity entity = response.getEntity();
             String html = EntityUtils.toString(entity, "GBK");
			 Document doc = Jsoup.parse(html);
			 
			 Element masthead = doc.getElementById("table_match");
		   	 Elements links = masthead.select("tr");
			 textarea.append("连接成功！\n");
			 textarea.setCaretPosition(textarea.getText().length());
			 int count = 0;
		   	 for (Element str : links)
		   	 {
		   		if(!str.attr("parentid").equals(""))
		   		    continue;
		   		if(count == 0)
		  		{
		   			count++;
		  			continue;
		  		}
		   		if(start_over)
	    			break;
		   		ArrayList<String> rowdata0 = getmsg(str,count,doc,r[0]);
		   		if(rowdata0 == null)
		   			continue;
		   		String[] rowdata = rowdata0.toArray(new String[rowdata0.size()]);
		   	    ((DefaultTableModel)table.getModel()).addRow(rowdata);
		   	    count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(wrong_time >= 8)
			{
				textarea.append("网络连接错误！\n");
				start_over = true;
				while(!change){Thread.sleep(1000);}
				return;
			}
			wrong_time++;
			System.out.println("主页面连接超时！！");
			System.out.println("----------------------");
			textarea.append("比赛页面连接超时！\n重新连接\n");
			textarea.setCaretPosition(textarea.getText().length());
			((DefaultTableModel)table.getModel()).getDataVector().removeAllElements(); 
			init();
		} 
		catch (NullPointerException e) {
			textarea.append("找不到此期号！\n");
			e.printStackTrace();
			//System.out.println("找不到当前期号！");
			while(!change){Thread.sleep(1000);}
			wrong_time = 0;
			start_over = true;
		} //catch (IndexOutOfBoundsException e) {
			//System.out.println("错误！！");
			//return;
		//}
	}
	//数据刷新
	public void refresh() throws InterruptedException
	{
		try {
			 Q.clear();
			 Q.add(new InfoStore("-","-","-","-","-","-","-","-"));
			 if(start_over)
	   			  return;
			 String[] r = set_pre();
			 CloseableHttpClient httpclient = HttpClients.createDefault();
             HttpGet httpget = new HttpGet(r[2]);
             HttpResponse response = httpclient.execute(httpget);
             HttpEntity entity = response.getEntity();
             String html = EntityUtils.toString(entity, "GBK");
			 Document doc = Jsoup.parse(html);
			 
			 Element masthead = doc.getElementById("table_match");
		   	 Elements links = masthead.select("tr");
			 int count=0;
		   	 for (Element str : links)
		   	 {
		   		if(!str.attr("parentid").equals(""))
		   		    continue;
		   		 if(count==0)
		   		 {
		   			count++;
		   			continue;
		   		 }
		   		 if(start_over)
		   			 break;
		   		 ArrayList<String> rowdata0 = getmsg(str,count,doc,r[0]);
		   		 if(rowdata0 == null)
		   			 continue;
		   		 String[] rowdata = rowdata0.toArray(new String[rowdata0.size()]);
		   		 //System.out.println(rowdata[2] + "   " + count);
		   		 String strs;
		   		 for(int i = 0 ; i < Titles.Head.length ; i++)
		   		 {
		   			 if(start_over)
			   			 break;
		   			 //String match_num = table.getValueAt(count-1, 0).toString();
		   			 if(table.getValueAt(count-1, i) == null)
		   				 break;
		   			 strs = table.getValueAt(count-1, i).toString();
		   			 if(!strs.equals(rowdata[i]))
		   			 {
		   				 /*System.out.println(table.getColumnName(i));
		   				 System.out.println("原场次：" + table.getValueAt(count-1, 2).toString());
		   				 System.out.println("场次：" + rowdata[2]);
		   				 System.out.println(rowdata[2] + " vs " + rowdata[4]);
		   				 System.out.println("由" + strs + " 更改为 " + rowdata[i]);*/
			   			 table.setValueAt(rowdata[i], count-1, i);
		   			 }
		   		 }
		   	     count++;
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(wrong_time >= 8)
			{
				textarea.append("网络连接错误！\n");
				start_over = true;
				while(!change){Thread.sleep(1000);}
				wrong_time = 0;
				return;
			}
			wrong_time++;
			refresh();
		} /*catch (IndexOutOfBoundsException e) {
			System.out.println("错误！！");
			return;
		}*/
	}
		
	public void change(int f)
	{
		try
		{
			if(f==1)//单场
			{
				CloseableHttpClient httpclient = HttpClients.createDefault();
	            HttpGet httpget = new HttpGet("http://live.500.com/zqdc.php");
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            String html = EntityUtils.toString(entity, "GBK");
				Document doc = Jsoup.parse(html);
				
				Element masthead = doc.getElementById("sel_expect");
				String nowstr = masthead.select("option").text();
				String numstr1=nowstr.substring(4, 6);
				String numstr2=nowstr.substring(2, 4);
				String numstr3=nowstr.substring(0, 2);
				String s1,s2,s3;
				int d = Integer.parseInt(numstr1);
				int m = Integer.parseInt(numstr2);
				int y = Integer.parseInt(numstr3);
				int nd = 20, nm = m, ny = y;
				int count1 = 0,count2 = 0;
				while(count1 < 36){
					count2 = 0;
					nd = 20;
					while(count2 < 20){
						if(ny == y && nm == m && nd > d)
						{
							count2++;
							nd--;
							continue;
						}
						if(nd < 10)
							s1 = "0"+Integer.toString(nd);
						else
							s1 = Integer.toString(nd);
						if(nm < 10)
							s2 = "0"+Integer.toString(nm);
						else
							s2 = Integer.toString(nm);
						if(ny < 10)
							s3 = "0"+Integer.toString(ny);
						else
							s3 = Integer.toString(ny);
						choice.add("       "+ s3 + s2 + s1 + "       ");
						count2++;
						nd--;
					}
					count1++;
					nm--;
					if(nm == 0){
						nm = 12;
						ny--;
					}
				}
			}
			else//足彩
			{
				CloseableHttpClient httpclient = HttpClients.createDefault();
	            HttpGet httpget = new HttpGet("http://live.500.com/zucai.php");
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            String html = EntityUtils.toString(entity, "GBK");
				Document doc = Jsoup.parse(html);
				
				Element masthead = doc.getElementById("sel_expect");
				String nowstr = masthead.select("option").text();
				String numstr=nowstr.substring(2, 5);
				String s;
				int count = 0;
				int i = Integer.parseInt(numstr);
				for(int t = i ; t > 0 ; t--)
				{
					if(count > 50)
						break;
					if(t < 10)
						s = "00"+Integer.toString(t);
					else if(t<100)
						s = "0"+Integer.toString(t);
					else
						s = Integer.toString(t);
			    	choice.add("       "+nowstr.substring(0,2) + s + "        ");
			    	count++;
				}
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void itemStateChanged(ItemEvent e) 
	{
		   //获取改变的复选按键
	       Object source = e.getItemSelectable();
	       String str = "";
	       if(e.getStateChange() == ItemEvent.SELECTED)
	    	   str = "show";
	       if(e.getStateChange() == ItemEvent.DESELECTED)
	    	   str = "hide";
	       if(source == CheckBox.t1){
	    	   String[] col = {Titles.h1_1,Titles.h1_2,Titles.h1_3};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t2){
	    	   String[] col = {Titles.h2_1,Titles.h2_2,Titles.h2_3,Titles.h2_4,Titles.h2_5,Titles.h2_6,Titles.h2_7};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t3){
	    	   String[] col = {Titles.h3_1,Titles.h3_2,Titles.h3_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t4){
	    	   String[] col = {Titles.h4_1,Titles.h4_2,Titles.h4_3,Titles.h4_4,Titles.h4_5,
	    			   Titles.h4_6,Titles.h4_7};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t5){
	    	   String[] col = {Titles.h5_1,Titles.h5_2,Titles.h5_3,Titles.h5_4,Titles.h5_5,Titles.h5_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t6){
	    	   String[] col = {Titles.h6_1,Titles.h6_2,Titles.h6_3,Titles.h6_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t7){
	    	   String[] col = {Titles.h7_1,Titles.h7_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t8){
	    	   String[] col = {Titles.h8_1,Titles.h8_2,Titles.h8_3,Titles.h8_4,Titles.h8_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t9){
	    	   String[] col = {Titles.h9_1,Titles.h9_2,Titles.h9_3,Titles.h9_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t10){
	    	   String[] col = {Titles.h10_1,Titles.h10_2,Titles.h10_3,Titles.h10_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t11){
	    	   String[] col = {Titles.h11_1,Titles.h11_2,Titles.h11_3,Titles.h11_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t12){
	    	   String[] col = {Titles.h12_1};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t13){
	    	   String[] col = {Titles.h13_1,Titles.h13_2,Titles.h13_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t14){
	    	   String[] col = {Titles.h14_1,Titles.h14_2,Titles.h14_3,Titles.h14_4,Titles.h14_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t15){
	    	   String[] col = {Titles.h15_1,Titles.h15_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t16){
	    	   String[] col = {Titles.h16_1,Titles.h16_2,Titles.h16_3,Titles.h16_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t17){
	    	   String[] col = {Titles.h17_1,Titles.h17_2,Titles.h17_3,Titles.h17_4,Titles.h17_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t18){
	    	   String[] col = {Titles.h18_1,Titles.h18_2,Titles.h18_3,Titles.h18_4,Titles.h18_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t19){
	    	   String[] col = {Titles.h19_1,Titles.h19_2,Titles.h19_3,Titles.h19_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t20){
	    	   String[] col = {Titles.h20_1,Titles.h20_2,Titles.h20_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t21){
	    	   String[] col = {Titles.h21_1,Titles.h21_2,Titles.h21_3,Titles.h21_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t22){
	    	   String[] col = {Titles.h22_1,Titles.h22_2,Titles.h22_3,Titles.h22_4,Titles.h22_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t23){
	    	   String[] col = {Titles.h23_1,Titles.h23_2,Titles.h23_3,Titles.h23_4,Titles.h23_5,
	    			   Titles.h23_6,Titles.h23_7};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t24){
	    	   String[] col = {Titles.h24_1,Titles.h24_2,Titles.h24_3,Titles.h24_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t25){
	    	   String[] col = {Titles.h25_1,Titles.h25_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t26){
	    	   String[] col = {Titles.h26_1,Titles.h26_2,Titles.h26_3,Titles.h26_4,Titles.h26_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t27){
	    	   String[] col = {Titles.h27_1,Titles.h27_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t28){
	    	   String[] col = {Titles.h28_1,Titles.h28_2,Titles.h28_3,Titles.h28_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t29){
	    	   String[] col = {Titles.h29_1,Titles.h29_2,Titles.h29_3,Titles.h29_4,Titles.h29_5,
	    			   Titles.h29_6,Titles.h29_7,Titles.h29_8,Titles.h29_9,Titles.h29_10};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t30){
	    	   String[] col = {Titles.h30_1,Titles.h30_2,Titles.h30_3,Titles.h30_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t31){
	    	   String[] col = {Titles.h31_1,Titles.h31_2,Titles.h31_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t32){
	    	   String[] col = {Titles.h32_1,Titles.h32_2,Titles.h32_3,Titles.h32_4,Titles.h32_5,Titles.h32_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t33){
	    	   String[] col = {Titles.h33_1,Titles.h33_2,Titles.h33_3,Titles.h33_4,Titles.h33_5,Titles.h33_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t34){
	    	   String[] col = {Titles.h34_1,Titles.h34_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t35){
	    	   String[] col = {Titles.h35_1,Titles.h35_2,Titles.h35_3,Titles.h35_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t36){
	    	   String[] col = {Titles.h36_1,Titles.h36_2,Titles.h36_3,Titles.h36_4,Titles.h36_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t37){
	    	   String[] col = {Titles.h37_1,Titles.h37_2,Titles.h37_3,Titles.h37_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t38){
	    	   String[] col = {Titles.h38_1,Titles.h38_2,Titles.h38_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t40){
	    	   String[] col = {Titles.h40_1,Titles.h40_2,Titles.h40_3,Titles.h40_4,Titles.h40_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t41){
	    	   String[] col = {Titles.h41_1,Titles.h41_2,Titles.h41_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t42){
	    	   String[] col = {Titles.h42_1,Titles.h42_2,Titles.h42_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t43){
	    	   String[] col = {Titles.h43_1,Titles.h43_2,Titles.h43_3,Titles.h43_4,Titles.h43_5,Titles.h43_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t44){
	    	   String[] col = {Titles.h44_1};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t45){
	    	   String[] col = {Titles.h45_1};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t46){
	    	   String[] col = {Titles.h46_1};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t47){
	    	   String[] col = {Titles.h47_1,Titles.h47_2,Titles.h47_3,Titles.h47_4,Titles.h47_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t48){
	    	   String[] col = {Titles.h48_1,Titles.h48_2,Titles.h48_3,Titles.h48_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t49){
	    	   String[] col = {Titles.h49_1,Titles.h49_2,Titles.h49_3,Titles.h49_4,Titles.h49_5,Titles.h49_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t50){
	    	   String[] col = {Titles.h50_1,Titles.h50_2,Titles.h50_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t51){
	    	   String[] col = {Titles.h51_1,Titles.h51_2,Titles.h51_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t52){
	    	   String[] col = {Titles.h52_1,Titles.h52_2,Titles.h52_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t53){
	    	   String[] col = {Titles.h53_1,Titles.h53_2,Titles.h53_3,Titles.h53_4,Titles.h53_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t54){
	    	   String[] col = {Titles.h54_1,Titles.h54_2,Titles.h54_3,Titles.h54_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t56){
	    	   String[] col = {Titles.h56_1,Titles.h56_2,Titles.h56_3,Titles.h56_4,Titles.h56_5,Titles.h56_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t57){
	    	   String[] col = {Titles.h57_1,Titles.h57_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t58){
	    	   String[] col = {Titles.h58_1,Titles.h58_2,Titles.h58_3,Titles.h58_4,Titles.h58_5,Titles.h58_6,Titles.h58_7};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t59){
	    	   String[] col = {Titles.h59_1,Titles.h59_2,Titles.h59_3,Titles.h59_4,Titles.h59_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t62){
	    	   String[] col = {Titles.h62_1,Titles.h62_2,Titles.h62_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t63){
	    	   String[] col = {Titles.h63_1,Titles.h63_2,Titles.h63_3,Titles.h63_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t64){
	    	   String[] col = {Titles.h64_1,Titles.h64_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t65){
	    	   String[] col = {Titles.h65_1,Titles.h65_2,Titles.h65_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t68){
	    	   String[] col = {Titles.h68_1,Titles.h68_2,Titles.h68_3,Titles.h68_4,Titles.h68_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t70){
	    	   String[] col = {Titles.h70_1,Titles.h70_2,Titles.h70_3,Titles.h70_4,Titles.h70_5,
	    			   Titles.h70_6,Titles.h70_7,Titles.h70_8,Titles.h70_9};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t71){
	    	   String[] col = {Titles.h71_1,Titles.h71_2,Titles.h71_3,Titles.h71_4,Titles.h71_5,
	    			   Titles.h71_6,Titles.h71_7,Titles.h71_8};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t72){
	    	   String[] col = {Titles.h72_1,Titles.h72_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t73){
	    	   String[] col = {Titles.h73_1,Titles.h73_2,Titles.h73_3,Titles.h73_4,Titles.h73_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t76){
	    	   String[] col = {Titles.h76_1,Titles.h76_2,Titles.h76_3,Titles.h76_4,Titles.h76_5,Titles.h76_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t77){
	    	   String[] col = {Titles.h77_1,Titles.h77_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t78){
	    	   String[] col = {Titles.h78_1,Titles.h78_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t79){
	    	   String[] col = {Titles.h79_1,Titles.h79_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t80){
	    	   String[] col = {Titles.h80_1,Titles.h80_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t81){
	    	   String[] col = {Titles.h81_1,Titles.h81_2,Titles.h81_3,Titles.h81_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t82){
	    	   String[] col = {Titles.h82_1,Titles.h82_2,Titles.h82_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t83){
	    	   String[] col = {Titles.h83_1,Titles.h83_2,Titles.h83_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t85){
	    	   String[] col = {Titles.h85_1,Titles.h85_2,Titles.h85_3,Titles.h85_4,Titles.h85_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t86){
	    	   String[] col = {Titles.h86_1,Titles.h86_2,Titles.h86_3,Titles.h86_4,Titles.h86_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t87){
    		   String[] col = {Titles.h87_1,Titles.h87_2,Titles.h87_3,Titles.h87_4,Titles.h87_5,Titles.h87_6};
	    		   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t88){
    		   String[] col = {Titles.h88_1};
	    		   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t89){
        	   String[] col = {Titles.h89_1,Titles.h89_2,Titles.h89_3,Titles.h89_4,Titles.h89_5,Titles.h89_6,Titles.h89_7,Titles.h89_8};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t90){
	    	   String[] col = {Titles.h90_1,Titles.h90_2,Titles.h90_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t91){
    		   String[] col = {Titles.h91_1,Titles.h91_2,Titles.h91_3,Titles.h91_4,Titles.h91_5,Titles.h91_6};
	    		   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t92){
	    	   String[] col = {Titles.h92_1,Titles.h92_2,Titles.h92_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t93){
	    	   String[] col = {Titles.h93_1,Titles.h93_2,Titles.h93_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t94){
	    	   String[] col = {Titles.h94_1,Titles.h94_2,Titles.h94_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t96){
	    	   String[] col = {Titles.h96_1,Titles.h96_2,Titles.h96_3,Titles.h96_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t97){
	    	   String[] col = {Titles.h97_1,Titles.h97_2,Titles.h97_3,Titles.h97_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t99){
	    	   String[] col = {Titles.h99_1,Titles.h99_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t100){
	    	   String[] col = {Titles.h100_1,Titles.h100_2,Titles.h100_3,Titles.h100_4,Titles.h100_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t101){
	    	   String[] col = {Titles.h101_1,Titles.h101_2,Titles.h101_3,Titles.h101_4,Titles.h101_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t102){
	    	   String[] col = {Titles.h102_1,Titles.h102_2,Titles.h102_3,Titles.h102_4,Titles.h102_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t103){
	    	   String[] col = {Titles.h103_1,Titles.h103_2};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t104){
	    	   String[] col = {Titles.h104_1,Titles.h104_2,Titles.h104_3,Titles.h104_4,Titles.h104_5,Titles.h104_6,Titles.h104_7};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t105){
	    	   String[] col = {Titles.h105_1,Titles.h105_2,Titles.h105_3,Titles.h105_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t106){
	    	   String[] col = {Titles.h106_1,Titles.h106_2,Titles.h106_3,Titles.h106_4,Titles.h106_5,Titles.h106_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t114){
	    	   String[] col = {Titles.h114_1,Titles.h114_2,Titles.h114_3,Titles.h114_4,Titles.h114_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t115){
	    	   String[] col = {Titles.h115_1,Titles.h115_2,Titles.h115_3,Titles.h115_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t117){
	    	   String[] col = {Titles.h117_1,Titles.h117_2,Titles.h117_3,Titles.h117_4,Titles.h117_5,Titles.h117_6,Titles.h117_7};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t118){
	    	   String[] col = {Titles.h118_1,Titles.h118_2,Titles.h118_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t124){
	    	   String[] col = {Titles.h124_1,Titles.h124_2,Titles.h124_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t126){
	    	   String[] col = {Titles.h126_1,Titles.h126_2,Titles.h126_3,Titles.h126_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t127){
	    	   String[] col = {Titles.h127_1,Titles.h127_2,Titles.h127_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t128){
	    	   String[] col = {Titles.h128_1,Titles.h128_2,Titles.h128_3,Titles.h128_4,Titles.h128_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t130){
	    	   String[] col = {Titles.h130_1,Titles.h130_2,Titles.h130_3,Titles.h130_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t133){
	    	   String[] col = {Titles.h133_1,Titles.h133_2,Titles.h133_3,Titles.h133_4,Titles.h133_5,Titles.h133_6,Titles.h133_7};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t137){
	    	   String[] col = {Titles.h137_1,Titles.h137_2,Titles.h137_3,Titles.h137_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t138){
	    	   String[] col = {Titles.h138_1,Titles.h138_2,Titles.h138_3,Titles.h138_4,Titles.h138_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t139){
	    	   String[] col = {Titles.h139_1,Titles.h139_2,Titles.h139_3,Titles.h139_4,Titles.h139_5};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t141){
	    	   String[] col = {Titles.h141_1,Titles.h141_2,Titles.h141_3,Titles.h141_4,Titles.h141_5,Titles.h141_6,Titles.h141_7,Titles.h141_8};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t142){
	    	   String[] col = {Titles.h142_1,Titles.h142_2,Titles.h142_3,Titles.h142_4};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t144){
	    	   String[] col = {Titles.h144_1,Titles.h144_2,Titles.h144_3};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t145){
	    	   String[] col = {Titles.h145_1,Titles.h145_2,Titles.h145_3,Titles.h145_4,Titles.h145_5,Titles.h145_6};
	    	   hide_show_column(col,str);
    	   }
    	   else if(source == CheckBox.t147){
	    	   String[] col = {Titles.h147_1,Titles.h147_2,Titles.h147_3,Titles.h147_4};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t149){
	    	   String[] col = {Titles.h149_1,Titles.h149_2,Titles.h149_3};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t151){
	    	   String[] col = {Titles.h151_1};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t153){
	    	   String[] col = {Titles.h153_1,Titles.h153_2,Titles.h153_3,Titles.h153_4,Titles.h153_5};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t154){
	    	   String[] col = {Titles.h154_1,Titles.h154_2};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t160){
	    	   String[] col = {Titles.h160_1,Titles.h160_2,Titles.h160_3,Titles.h160_4};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t163){
	    	   String[] col = {Titles.h163_1,Titles.h163_2,Titles.h163_3,Titles.h163_4,Titles.h163_5};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t167){
	    	   String[] col = {Titles.h167_1,Titles.h167_2,Titles.h167_3,Titles.h167_4,Titles.h167_5,Titles.h167_6,Titles.h167_7};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t169){
	    	   String[] col = {Titles.h169_1,Titles.h169_2,Titles.h169_3,Titles.h169_4};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t179){
	    	   String[] col = {Titles.h179_1,Titles.h179_2,Titles.h179_3,Titles.h179_4,Titles.h179_5,Titles.h179_6,Titles.h179_7};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t188){
	    	   String[] col = {Titles.h188_1,Titles.h188_2,Titles.h188_3,Titles.h188_4,Titles.h188_5,Titles.h188_6};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == CheckBox.t189){
	    	   String[] col = {Titles.h189_1,Titles.h189_2};
	    	   hide_show_column(col,str);
    	   }
	       else if(source == choice0)
	       {
	    	    ArrayList<String> Index;
				try {
					Index = SetDC(choice0.getSelectedItem(),36);
					choice1.removeAll();
					for(String s : Index)
						choice1.add(s);
					choice1.select(Index.get(Index.size()-1));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	       }
	       else if(source == choice1)
	       {
	    	   /*sta_over = true;
	    	   while(t.isAlive()){}
    		   t = new Thread(ds);
    		   t.start();
    		   System.out.println("Start");*/
	       }
	       else
	       {
	    	   if(RB5.isSelected())
	    		   leixing = 5;
	    	   start_over=true;
	    	   change = true;
	       }
	}
	public void hide_show_column(String[] col,String mode)
	{
		if(mode.equals("hide"))
		{
			for(int i = 0 ; i < col.length ; i++)
			{
			    table.getColumnModel().getColumn(tableModel.findColumn(col[i])).setMinWidth(0);
	 		    table.getColumnModel().getColumn(tableModel.findColumn(col[i])).setMaxWidth(0);
	 		    table.getTableHeader().getColumnModel().getColumn(tableModel.findColumn(col[i])).setMinWidth(0);
		 		table.getTableHeader().getColumnModel().getColumn(tableModel.findColumn(col[i])).setMaxWidth(0);
			}
		}
		else if(mode.equals("show"))
		{
			for(int i = 0 ; i < col.length ; i++)
			{
				table.getColumnModel().getColumn(tableModel.findColumn(col[i])).setMinWidth(80);
		 		table.getColumnModel().getColumn(tableModel.findColumn(col[i])).setMaxWidth(500);
		 		table.getTableHeader().getColumnModel().getColumn(tableModel.findColumn(col[i])).setMinWidth(80);
		 		table.getTableHeader().getColumnModel().getColumn(tableModel.findColumn(col[i])).setMaxWidth(500);
			}
		}
	}
	class Buttonclicked implements ActionListener   //按钮监听
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==Button1)
			{
				start_over=true;
				change = true;
			}
			else if(e.getSource()==Button2)
			{
				String s_str = "",str;
				try {
					URL url = Lists.class.getResource("/display/rules");  
					InputStream in = url.openStream();  
					//File file = new File("/jsoup/rules");
					InputStreamReader read = new InputStreamReader(in, "UTF-8");
	                BufferedReader br = new BufferedReader(read);
					while ((str = br.readLine()) != null)
						s_str += ( str + "\n");
					br.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				TextArea t = new TextArea(s_str,50,120);
				t.setFont(new Font("宋体",Font.BOLD,12)); 
				t.setEditable(false);
				JOptionPane.showConfirmDialog(null,new JScrollPane(t),"规 则 解 释",JOptionPane.CLOSED_OPTION,JOptionPane.INFORMATION_MESSAGE);
			}
			else if(e.getSource()==Button3)
			{
				System.exit(1);
			}
			else if(e.getSource()==Button4)
			{
				playerInfo= new Object[][] {};
			    Names= new String[] {"场次","赛事","比赛时间","主队","客队","平均赔率-胜","平均赔率-平","平均赔率-负","方案一选号","方案二选号"};
			    tableModel1 = new DefaultTableModel(playerInfo,Names)
			    {
			    	/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					public boolean isCellEditable(int row, int column)
					{
			            return false;
			        }//表格不允许被编辑
			    };
			    rxj = new JTable(tableModel1);
			    String[][] mid = Choose_Nine.rxj();
			    for(int i = 0 ; i < 14 ; i++)
			    {
			    	Object[] rowdata = {i+1,Choose_Nine.msg[i].comp,Choose_Nine.msg[i].time,Choose_Nine.msg[i].zhu,
			    			Choose_Nine.msg[i].ke,Choose_Nine.msg[i].win,Choose_Nine.msg[i].draw,
			    			Choose_Nine.msg[i].lose,mid[i][0],mid[i][1]};
			    	((DefaultTableModel)rxj.getModel()).addRow(rowdata);
			    }
			    rxj.setPreferredScrollableViewportSize(new Dimension(1000,500));
			    DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
			    r.setHorizontalAlignment(JLabel.CENTER);   
			    rxj.setDefaultRenderer(Object.class,r);
				JOptionPane.showMessageDialog(null,new JScrollPane(rxj),"任 猜 9 场",JOptionPane.INFORMATION_MESSAGE,null);
			}
			else if(e.getSource() == Button5)
			{
				ds.s.Display();
			}
			else if(e.getSource() == Button6)
			{
				if(Button6.getText().equals("开 始 统 计"))
				{
					t = new Thread(ds);
					t.start();
					Button6.setText("停 止 统 计");
					choice0.setEnabled(false);
			    	choice1.setEnabled(false);
			    	Button5.setEnabled(true);
				}
				else
				{
					sta_over = true;
			    	while(t.isAlive()){}
			    	Button6.setText("开 始 统 计");
			    	choice0.setEnabled(true);
			    	choice1.setEnabled(true);
			    	Button5.setEnabled(false);
			    	datatext.setText("统计停止！");
				}
			}
		}
	}
	//JRadioGroup事件服务类
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getActionCommand() == "完整版")
		{
			panel3.setBorder(BorderFactory.createTitledBorder(""));
	    	choice.removeAll();
	    	choice.setEnabled(false);
	    	start_over=true;
	    	change = true;
	    	leixing=1;
		}
		else if(e.getActionCommand() == "单场比分")
		{
			panel3.setBorder(BorderFactory.createTitledBorder("期号选择"));
	    	choice.removeAll();
	    	choice.setEnabled(true);
	    	change(1);
	    	start_over=true;
	    	change = true;
	    	leixing=2;
		}
		else if(e.getActionCommand() == "竞彩比分")
		{
			panel3.setBorder(BorderFactory.createTitledBorder("日期选择"));
	    	choice.removeAll();
	    	choice.setEnabled(true);
	    	cal = Calendar.getInstance();
	   	    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	   	    cal.add(Calendar.DAY_OF_MONTH, 0);
	       	choice.add("     "+format.format(cal.getTime())+"     ");
	       	for(int i = 0 ; i < 20 ; i++)
	   	    {
	       		cal.add(Calendar.DAY_OF_MONTH, -1);
		        choice.add("     "+format.format(cal.getTime())+"     ");
		    }
	       	start_over=true;
	       	change = true;
	       	leixing=3;
		}
		else if(e.getActionCommand() == "足彩比分")
		{
			panel3.setBorder(BorderFactory.createTitledBorder("期号选择"));
	    	choice.removeAll();
	    	choice.setEnabled(true);
	    	change(0);
	    	start_over=true;
	    	change = true;
	    	leixing=4;
		}
		else if(e.getActionCommand() == "完场比分")
		{
			panel3.setBorder(BorderFactory.createTitledBorder("日期选择"));
	    	choice.removeAll();
	    	choice.setEnabled(true);
	    	cal = Calendar.getInstance();
	   	    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	       	for(int i = 0 ; i < 10 ; i++)
	   	    {
	       		cal.add(Calendar.DAY_OF_MONTH, -1);
		        choice.add("     "+format.format(cal.getTime())+"     ");
		    }
	       	start_over=true;
	       	change = true;
	       	leixing=5;
		}
		else if(e.getActionCommand() == "未来赛事")
		{
			panel3.setBorder(BorderFactory.createTitledBorder("日期选择"));
	    	choice.removeAll();
	    	choice.setEnabled(true);
	    	cal = Calendar.getInstance();
	   	    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	   	    cal.add(Calendar.DAY_OF_MONTH, 0);
	       	choice.add("     "+format.format(cal.getTime())+"     ");
	       	for(int i = 0 ; i < 10 ; i++)
	   	    {
	       		cal.add(Calendar.DAY_OF_MONTH, 1);
		        choice.add("     "+format.format(cal.getTime())+"     ");
		    }
	       	start_over=true;
	       	change = true;
	       	leixing=6;
		}
	}
	
	public static ArrayList<String> SetDC(String nowstr, int n) throws IOException
	{
		try{
			ArrayList<String> Index = new ArrayList<String>();
			if(nowstr.equals("")){
				CloseableHttpClient httpclient = HttpClients.createDefault();
	            HttpGet httpget = new HttpGet("http://live.500.com/zqdc.php");
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            String html = EntityUtils.toString(entity, "GBK");
				Document doc = Jsoup.parse(html);
				
				Element masthead = doc.getElementById("sel_expect");
				nowstr = masthead.select("option").text();
			}
			String numstr1= nowstr.substring(4, 6);
			String numstr2= nowstr.substring(2, 4);
			String numstr3= nowstr.substring(0, 2);
			String s1,s2,s3;
			int d = Integer.parseInt(numstr1);
			int m = Integer.parseInt(numstr2);
			int y = Integer.parseInt(numstr3);
			int nd = 20, nm = m, ny = y;
			int count1 = 0,count2 = 0;
			while(count1 < n){
				count2 = 0;
				nd = 20;
				while(count2 < 20){
					if(ny == y && nm == m && nd > d){
						count2++;
						nd--;
						continue;
					}
					if(nd < 10) s1 = "0"+Integer.toString(nd);
					else s1 = Integer.toString(nd);
					if(nm < 10) s2 = "0"+Integer.toString(nm);
					else s2 = Integer.toString(nm);
					if(ny < 10) s3 = "0"+Integer.toString(ny);
					else s3 = Integer.toString(ny);
					Index.add(s3 + s2 + s1);
					count2++;
					nd--;
				}
				count1++;
				nm--;
				if(nm == 0){
					nm = 12;
					ny--;
				}
			}
			return Index;
		}catch(IOException e){
			Lists.datatext.setText("数据统计网络连接错误！");
			return SetDC(nowstr, n);
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException 
    {
    	Lists list = new Lists();
    	while(true)
    	{
    		((DefaultTableModel)table.getModel()).getDataVector().removeAllElements();
    		change = false;
    		start_over = false;
    		long startTime=System.currentTimeMillis();
	    	list.init();
	    	if(start_over)
    			continue;
	    	long endTime=System.currentTimeMillis();
			System.out.println("初始化用时："+(endTime-startTime)+"ms");
	    	System.out.println("初始化完毕");
	    	int comp_num = table.getRowCount();
			textarea.append("初始化完毕\n");
			textarea.append("共有" + comp_num + "场比赛\n");
			textarea.setCaretPosition(textarea.getText().length());
	    	while(true)
	    	{
	    		list.refresh();
	    		if(start_over)
	    			break;
	    	}
    	}
    }
}
