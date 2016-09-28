package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import data_process.*;

public class Statistics implements ActionListener {
	public static DefaultTableModel tableModel2;
	public static JTable table2;
	ButtonGroup group = new ButtonGroup();
	Object[][] playerInfo2;
	String[] Names = {"主队胜","平局","主队负","未开始","进行中"};

	public TextArea textarea = new TextArea("单场结论数据统计\n",10,20);
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints parameter = new GridBagConstraints();
	JPanel panel = new JPanel(gridbag);
	GridBagLayout gridbag1 = new GridBagLayout();    //设置为网格袋布局
	GridBagConstraints parameter1 = new GridBagConstraints();
	JPanel panel2 = new JPanel(gridbag1);
	GridBagLayout gridbag3 = new GridBagLayout();
	GridBagConstraints parameter3 = new GridBagConstraints();
	JPanel panel3 = new JPanel(gridbag3);
	HashMap<String,Data> Data_Map = new HashMap<String,Data>();
	static ChartFrame frame;
	JFrame f;
	int sum;
	static ArrayList<InfoStore> Q = new ArrayList<InfoStore>();
	public Statistics() throws IOException
	{
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    playerInfo2= new Object[][] {};
	    tableModel2 = new DefaultTableModel(playerInfo2,Names)
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
	    table2 = new JTable(tableModel2);
	    table2.getTableHeader().setReorderingAllowed(false);
	    table2.setPreferredScrollableViewportSize(new Dimension(300,900));
    	for(int i = 0 ; i < Names.length ; i++)
	    {
    		table2.getColumnModel().getColumn(i).setMinWidth(400);
    	    table2.getColumnModel().getColumn(i).setMaxWidth(800);
	    }
	    table2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		panel2.setBorder(BorderFactory.createTitledBorder("类型选择")); //设置两组单选按钮面板边框
	    //panel2.setLayout(new GridLayout(30,3,20,0));
	    for(int i = 0 ; i < RadioButton.RB.length ; i++)
	    {
	    	group.add(RadioButton.RB[i]);
	    	RadioButton.RB[i].addActionListener(this);
	    	if((i+1) % 3 != 0)
	    		parameter1.gridwidth = GridBagConstraints.BOTH;
	    	else
	    		parameter1.gridwidth = GridBagConstraints.REMAINDER;
		    parameter1.insets=new Insets(0,10,0,10); 
			gridbag1.setConstraints(RadioButton.RB[i], parameter1);
			panel2.add(RadioButton.RB[i]);
	    }
		textarea.setEditable(false);
	    parameter.anchor=GridBagConstraints.CENTER;
		parameter.fill = GridBagConstraints.BOTH;
		parameter.gridwidth = GridBagConstraints.REMAINDER;
		parameter.insets=new Insets(10,50,0,50);
		gridbag.setConstraints(textarea, parameter);
		panel.add(textarea);
		parameter.insets=new Insets(0,50,10,50);
		gridbag.setConstraints(panel2, parameter);
		panel.add(panel2);
		
	    JScrollPane scrollPane=new JScrollPane();
	    scrollPane.setViewportView(table2);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    f.getContentPane().add(scrollPane,BorderLayout.CENTER);
	    JScrollPane js = new JScrollPane(panel);
	    js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    f.getContentPane().add(js,BorderLayout.EAST);
	}
	public void Display()
	{
		f.setTitle("单场数据统计");
	    f.pack();
	    f.setExtendedState(Frame.MAXIMIZED_BOTH );
	    f.setVisible(true);
	}
	public String[] GetMsg(Element str, int count, Document doc, String DC) throws IOException
	{
		 String time = str.select("td").get(3).text();
 		 String state = str.select("td").get(4).text().replace(Jsoup.parse("&nbsp;").text(), "进行中");
  		 String comp  = str.select("a").get(0).text();
  		 String team1 = str.select("a").get(1).text();
  		 String score1 = str.select("a").get(2).text();
  		 String score2 = str.select("a").get(4).text();
  		 String team2 = str.select("a").get(5).text();
  		 String index = str.select("td").get(0).text();
  		 String half = str.select("td").get(8).text();
  		 
  		 int choose = 11;
 		 if(str.select("td").get(11).select("a").size() < 3)
 		 {
 			 if(str.select("td").get(10).select("a").size() < 3)
 				 return null;
 			 else
 				choose = 10;
 		 }
  		 String linkHrefxi = str.select("td").get(choose).select("a").get(0).attr("href");
 		 String linkHrefya = str.select("td").get(choose).select("a").get(1).attr("href");
 		 String linkHrefou = str.select("td").get(choose).select("a").get(2).attr("href");
 		 String linkHrefdx = linkHrefou.substring(0,linkHrefou.indexOf("ouzhi"))
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
		 Path+="单场比分\\" + DC;
 		 Europe Oz = new Europe(linkHrefou,Path,index,state);
 		 double O[] = Oz.ouzhi();
 		 double AveO10[] = Oz.Average_Odds(10, "n");
 		 double AveO20[] = Oz.Average_Odds(20, "n");
 		 Asia Yz = new Asia(linkHrefya,Path,index,state);
 		 double[] Y = Yz.yazhi();
 		 Analysis Fx = new Analysis(linkHrefxi,Path,index,state);
 		 double[] F = Fx.fenxi();
 		 Comparison Dx = new Comparison(linkHrefdx,Path,index,state);
 		 double[] D = Dx.daxiao();
 		 

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
 		 
 		 //项目3-S1
		 double g_p3a = F[0];
 		 double g_p3b = Fx.Match_Condition((int)F[0], "d1");
 		 double g_p3c = Dx.Match_Condition(10, "np<3.5&1.00<nd");
 		 String r_3 = g_p3a > 2 && g_p3b == F[0] && g_p3c >= 4? "S1":"-";
 		 //项目2-Z4
		 String p2a = Fx.Get_SF("f1", 0);
		 String p2b = Fx.Get_SF("f4", 0);
		 String p2c = Fx.Get_SF("s5", 0);
		 double g_p2d = Oz.Company_state("威廉希尔", "nd");
		 double g_p2e = Yz.Match_Condition(10, "pp=-0.5&1.005<plw");
		 double g_p2f = Yz.Match_Condition(10, "np=-0.25&1.005<nlw");
		 double g_p2g = Yz.Match_Condition(10, "np=0");
		 String r_2 = p2a.equals("负") && p2b.equals("负") && p2c.equals("胜") && g_p2d > 3.40 && g_p2e > 5 &
				 g_p2f > 5 && g_p2g > 2? "Z4":"-";
 		 //项目4-S2
 		 double g_p4a = Fx.Series(10,"d4");
		 double g_p4b = Fx.Series(10,"d5");
		 double g_p4c = Dx.Match_Condition(20, "1.00<=nd");
 		 double g_p4d = Dx.Match_Condition(10, "3.5<=np");
		 String p4e = Fx.Get_SF("d4", 0);
		 String p4f = Fx.Get_SF("d5", 0);
		 double g_p4g = g_p3b;
		 String r_4 = (g_p4a > 4 | g_p4b > 4) && g_p4c > 10 && g_p4d < 5 && 
				 	p4e.equals("大") && p4f.equals("大") && g_p4g == F[0]? "S2":"-";
		 //项目5-Z5
		 double g_p5a = F[0];
		 String p5b = Fx.Match_Condition((int)F[0], "s1") == 0 && F[0] > 3? "Yes" : "No";
		 String p5c = Fx.Get_SF("s5", 0);
		 double g_p5d = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 double g_p5e = Yz.Match_Condition(10, "pp=-0.25&1.005<prw");
		 double g_p5f = Yz.Match_Condition(10, "np=-0.5");
		 String r_5 = g_p5a > 3 && p5b.equals("Yes") && p5c.equals("胜") && g_p5d == 2 && g_p5e > 5 && g_p5f > 7? "Z5":"-";
		 

		 //项目6-Z9
		 double g_p6a = Fx.Get_List("zzs");
		 double g_p6b = Fx.Get_List("kks");
		 double g_p6c = Yz.Match_Condition(10, "pp=-0.25&1.005<prw");
		 double g_p6d = Yz.Match_Condition(10, "np=-0.5");
		 String r_6 = g_p6a == 0 && g_p6b > 2 && g_p6c > 5 && g_p6d > 7? "Z9":"-";
		 
		 //项目7-S3
		 double g_p7a = Yz.Match_Condition(12, "np=-1.5");
		 double g_p7b = Dx.Match_Condition(12, "np=2.25");
		 String r_7 = g_p7a > 6 && g_p7b > 1? "S3":"-";
		 
		 //项目8-PF12
		 String p8a = Fx.Get_SF("s1", 0);
		 double g_p8b = Fx.Match_Condition(3, "s4");
		 String p8c = Fx.Get_SF("s5", 0);
		 double g_p8d = Yz.Match_Condition(10, "pp=-0.25");
		 double g_p8e = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 String r_8 = (p8a.equals("胜")|p8a.equals("平")) && g_p8b == 3 && p8c.equals("负") && g_p8d > 5 && g_p8e > 4? "PF12":"-";
		 
		 //项目9-YQXP4
		 double g_p9a = Yz.Match_Condition(12, "np=-1&1.010<=nlw");
		 double g_p9b = Yz.Match_Condition(12, "np=-0.75&1.010<=nlw");
		 double g_p9c = Yz.Match_Condition(12, "np=-0.5&nlw<=0.78");
		 double g_p9d = Fx.Match_Condition(2, "s5");
		 String r_9 = g_p9a > 0 && g_p9b > 0 && g_p9c > 0 && g_p9d <= 1? "YQXP4":"-";

		 //项目10-Z19
		 String p10a = Fx.Get_SF("s4", 0);
		 double g_p10b = Yz.Match_Condition(10, "1.10<plw");
		 double g_p10c = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p10d = Yz.Match_Condition(10, "1.005<nrw");
		 String r_10 = p10a.equals("负") && g_p10b > 1 && g_p10c > 4 && g_p10d > 1? "Z19":"-";
		 
		 //项目11-QDBS3
		 double g_p11a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 double g_p11b = Yz.Match_Condition(10, "np=-1") + Yz.Match_Condition(10, "np=-1.25");
		 double g_p11c = Oz.Match_Condition(10, "nw<pw");
		 String g_p11d = Fx.Get_SF("s5", 0);
		 String r_11 = g_p11a == 2 && g_p11b > 4 && g_p11c > 5 && g_p11d.equals("负")? "QDBS3":"-";
		 
  		 //项目12-YK
  		 double g_p12a = Oz.Match_Condition(15, "pd<nd&nl<pl&nw=pw");
 		 String r_12 = g_p12a > 1? "YK":"-";
 		 
 		 //项目13-SF2
		 double g_p13a = Yz.Company_state("澳门", "np");
		 double g_p13b = Oz.Company_state("澳门", "nd");
		 double g_p13c = Oz.Company_state("威廉希尔", "nd");
		 String r_13 = g_p13a >= -0.25 && g_p13a <= 0.25 && g_p13b > 3.30 && g_p13c > 3.40? "SF2":"-";
 		 
		 //项目14-Z20
		 double g_p14a = Oz.Match_Condition(10, "nw=nl");
		 double g_p14b = Oz.Match_Condition(10, "pw<nw");
		 double g_p14c = Oz.Match_Condition(10, "nd<=pd");
		 double g_p14d = Oz.Match_Condition(10, "nl<pl");
		 String p14e = Fx.Get_SF("s4", 0);
		 String r_14 = g_p14a >= 1 && g_p14b > 5 && g_p14c > 5 && g_p14d > 5 && p14e.equals("胜")? "Z20":"-";
		 
		 //项目15-DF2
		 double g_p15a = Dx.Match_Condition(20, "3.5<=pp");
		 double g_p15b = Dx.Match_Condition(20, "3.5<=np&1.00<nd");
		 String r_15 = g_p15a > 12 && g_p15b > 4? "DF2":"-";
		 
		 //项目16-DD2
		 double g_p16a = Fx.GP_Condition(5, "0<kg2");
		 double g_p16b = Fx.GP_Condition(5, "0<zg3");
		 double g_p16c = D[1];
		 double g_p16d = Dx.Match_Condition(20, "1.00<nd");
		 String r_16 = g_p16a >= 4 && g_p16b >= 4 && g_p16c > 1 && g_p16d > 10? "DD2":"-";
		 
		 //项目17-DFF
		 double g_p17a = g_p16a;
		 double g_p17b = g_p16b;
		 double g_p17c = Dx.Match_Condition(15, "1.05<=nd");
		 double g_p17d = Fx.Match_Condition((int)F[0], "x1");
		 double g_p17e = Fx.Match_Condition(5, "x2") + Fx.Match_Condition(5, "x3");
		 String r_17 = g_p17a >= 4 && g_p17b >= 4 && g_p17c >= 5 && (g_p17d > 4 | g_p17e >= 8)? "DFF":"-";
		 
		 //项目18-K1
		 double g_p18a = Oz.Match_Condition(10, "nw=nl");
		 double g_p18b = Oz.Match_Condition(10, "nw<pw");
		 double g_p18c = Oz.Match_Condition(10, "nd<=pd");
		 double g_p18d = Oz.Match_Condition(10, "pl<nl");
		 String p18e = Fx.Get_SF("s5", 0);
		 String r_18 = g_p18a >= 1 && g_p18b > 5 && g_p18c > 5 && g_p18d > 5 && p18e.equals("胜")? "K1":"-";
		 
 		 //项目19-Z1
		 double g_p19a = F[0] - Fx.Match_Condition((int)F[0], "s1");
		 double g_p19b = 5-Fx.Match_Condition(5, "s2");
		 double g_p19c = Oz.Match_Condition(20, "nl=nd");
		 double g_p19d = Yz.Match_Condition(10, "1.005<nlw");
 		 String r_19 = (int)g_p19a > 5 && g_p19b == 5 && g_p19c >= 1 && g_p19d < 2? "Z1":"-";
 		 
 		 //项目20-ZP3
		 double g_p20a = Yz.Company_state("Interwetten", "np");
		 double g_p20b = Yz.Company_state("Interwetten", "nrw");
		 double g_p20c = Oz.Company_state("Interwetten", "nd");
		 String r_20 = g_p20a == 0 && g_p20b >= 1.300 && g_p20c >=3.40? "ZP3":"-";
			
		 //项目21-Z21
		 String p21a = Fx.Get_SF("f4", 0);
		 String p21b = Fx.Get_SF("s5", 0);
		 double g_p21c = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p21d = Y[9];
		 String r_21 = p21a.equals("负") && p21b.equals("胜") && g_p21c > 2 && g_p21d > 5? "Z21":"-";
		 
		 //项目22-Z6
		 double g_p22a = Fx.Match_Condition(5, "p4");
		 double g_p22b = Fx.Match_Condition(5, "s5") + Fx.Match_Condition(5, "p5");
		 double g_p22c = Oz.Match_Condition(10, "3.39<nd");
		 double g_p22d = Oz.Match_Condition(10, "nd=nl");
		 double g_p22e = Yz.Match_Condition(10, "1.005<nlw");
		 String r_22 = g_p22a >= 3 && g_p22b == 5 && g_p22c > 2 && g_p22d >= 1 && g_p22e > 1? "Z6":"-";
				 
		 //项目23-PB
		 double g_p23a = Oz.Average_Odds(4, "n")[0];
		 double g_p23b = Oz.Match_Condition(20, "nd<=3.30");
		 double g_p23c = O[1];
		 double g_p23d = Fx.Match_Condition(3, "f2");
		 double g_p23e = Fx.Match_Condition(3, "s3");
		 double g_p23f = 5-Fx.Match_Condition(5, "s2");
		 String p23g = Fx.Get_SF("s5", 0);
		 String r_23 = g_p23a >= 1.80 && g_p23a <= 2.00 && g_p23b > 7 && g_p23c > 0.6 && g_p23d >= 1 &
				 g_p23e >= 1 && g_p23f >= 3 && p23g.equals("胜")? "PB":"-";
 
		 //项目25-SF5
		 double g_p25a = Fx.Match_Condition(5, "p4");
		 double g_p25b = Oz.Match_Condition(10, "3.49<nd");
		 String r_25 = g_p25a >= 3 && g_p25b > 2? "SF5":"-";
		 
		 //项目26-Z22
		 String p26a = Fx.Get_SF("f4", 0);
		 String p26b = Fx.Get_SF("s5", 0);
		 double g_p26c = Oz.Match_Condition(10, "nw<pw");
		 double g_p26d = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p26e = Yz.Match_Condition(10, "1.005<nrw");
		 String r_26 = p26a.equals("负") && p26b.equals("胜") && g_p26c > 5 && g_p26d > 5 && g_p26e > 2? "Z22":"-";
		 
		 //项目28-PF4
		 double g_p28a = Fx.Get_List("kks");
		 double g_p28b = Oz.Match_Condition(10, "nl<pl");
		 double g_p28c = Oz.Match_Condition(10, "3.25<nd");
		 double g_p28d = Yz.Match_Condition(10, "np=0.25&1.005<nrw");
		 String r_28 = g_p28a == 0 && g_p28b > 7 && g_p28c > 4 && g_p28d > 6? "PF4":"-";
		 
		 //项目29-PF1
		 double g_p29a = 10-Fx.Match_Condition(10, "f2");;
		 double g_p29b = AveO20[0]; //>2.00
		 double g_p29c = AveO20[1]; //<3.30
		 double g_p29d = Yz.Match_Condition(10, "-0.25<=np&1.020<=nlw");
		 double g_p29e = Oz.Match_Condition(10, "nw<=pw");
		 double g_p29f = Fx.Match_Condition(4, "f5");
		 double g_p29g = 4-Fx.Match_Condition(4, "f4");
		 double g_p29h = Oz.Company_state("威廉希尔", "nd"); //>3.00
		 double g_p29i = Oz.Company_state("Ladbrokes (立博)", "nd"); //>3.00
		 double g_p29j = Math.abs(Fx.Get_Rank("z")-Fx.Get_Rank("k"));
		 if(Fx.Get_Rank("z")*Fx.Get_Rank("k") == 0) g_p29j = -1;
		 String r_29 = g_p29a >= 8 && g_p29b > 2.00 && g_p29c < 3.30 && g_p29d >= 2 && g_p29e >= 7 && g_p29f >= 2 && 
				 g_p29g == 4 && g_p29h > 3.00 && g_p29i > 3.00 && g_p29j > 5? "PF1":"-";
		 
		 //项目30-YQXP2
		 String g_p30a = Fx.Get_SF("s1", 0);
		 double g_p30b = Fx.Series(10, "s4");
		 String g_p30c = Fx.Get_SF("f5", 0);
		 double g_p30d = Yz.Match_Condition(10, "np=-1&1.005<nlw");
		 String r_30 = g_p30a.equals("胜") && g_p30b >= 3 && g_p30c.equals("负") && g_p30d > 4? "YQXP2":"-";
			
		 //项目31-PF10
		 double g_p31a = Fx.Get_List("kks");
		 double g_p31b = Oz.Match_Condition(10, "pw<nw");
		 double g_p31c = Yz.Match_Condition(10, "np=-0.25&1.005<nrw");
		 String r_31 = g_p31a == 0 && g_p31b > 7 && g_p31c > 7? "PF10":"-";
		 
		 //项目32-Z15
		 double g_p32a = Fx.Match_Condition(3, "p1") + Fx.Match_Condition(3, "f1");
		 double g_p32b = Fx.Match_Condition(3, "p2") + Fx.Match_Condition(3, "f2");
		 double g_p32c = Fx.Match_Condition(3, "s4") + Fx.Match_Condition(3, "p4");
		 double g_p32d = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 double g_p32e = Oz.Match_Condition(10, "pw<nw");
		 double g_p32f = Oz.Match_Condition(10, "nd=nl");
		 String r_32 = g_p32a == 3 && g_p32b == 3 && g_p32c == 3 && g_p32d > 4 && g_p32e > 5 && g_p32f > 2? "Z15":"-";
		 
		 //项目33-K
		 double p33a1 = Oz.Company_state("Ladbrokes (立博)", "nd");
		 double p33a2 = Oz.Company_state("威廉希尔", "nd");
		 double g_p33a = p33a1 != -10 && p33a2 != -10? p33a1 - p33a2 : 0;
		 double g_p33b = Oz.Match_Condition(10, "nw<pw");
		 double g_p33c = Y[7];
		 double g_p33d = Yz.Match_Condition(10, "1.005<nlw");
		 String g_p33e = Fx.Get_SF("s4", 0);
		 String g_p33f = Fx.Get_SF("s5", 0);
		 String r_33 = g_p33a > 0.35 && g_p33b > 5 && g_p33c > 5 && g_p33d >= 1 &
				 (g_p33e.equals("平") | g_p33e.equals("负")) && (g_p33f.equals("平") | g_p33f.equals("负"))? "K":"-";
		 
		 //项目34-YQXP1
		 double g_p34a = Yz.Match_Condition(15, "np=-1.5&1.000<=nlw");
		 double g_p34b = Dx.Match_Condition(15, "1.000<=nd");
		 String r_34 = g_p34a >= 7 && g_p34b >= 7? "YQXP1":"-";
		 
		 //项目35-SF1
		 double g_p35a = Fx.Match_Condition(4, "s1");
		 double g_p35b = 5-Fx.Match_Condition(5, "f2");
		 double g_p35c = Yz.Match_Condition(15, "pp<np&1.000<=nlw&1.000<=plw");
		 double g_p35d = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl");
		 String r_35 = g_p35a == 4 && g_p35b == 5 && g_p35c > 4 && g_p35d > 8? "SF1":"-";
		 
		 //项目36-YQXP3
		 String g_p36a = Fx.Get_SF("s4", 0);
		 String g_p36b = Fx.Get_SF("s5", 0);
		 String g_p36c = Fx.Get_SF("s1", 0);
		 double g_p36d = Oz.Match_Condition(10, "nw<pw");
		 double g_p36e = Yz.Match_Condition(10, "1.005<nlw");
		 String r_36 = g_p36a.equals("胜") && g_p36b.equals("负") && g_p36c.equals("胜") && g_p36d > 5 && g_p36e > 4? "YQXP3":"-";
		 
		 //项目37-K2
		 double g_p37a = Yz.Match_Condition(10, "np=0.5&1.000<=nrw");
		 double g_p37b = Oz.Match_Condition(20, "nd=nw");
		 double g_p37c = Fx.Match_Condition(5, "s4");
		 double g_p37d = Fx.Match_Condition(5, "f5");
		 String r_37 = g_p37a >= 3 && g_p37b >= 1 && g_p37c >= 3 && g_p37d >= 3? "K2":"-";
		 
		 //项目39-Z23
		 String p39a = Fx.Get_SF("f4", 0);
		 String p39b = Fx.Get_SF("s5", 0);
		 double g_p39c = Yz.Match_Condition(10, "1.005<nrw");
		 double g_p39d = Y[10];
		 double g_p39e = Oz.Match_Condition(10, "nw=nl");
		 double g_p39f = Oz.Match_Condition(10, "nw=nd");
		 String r_39 = p39a.equals("负") && p39b.equals("胜") && g_p39c > 5 && g_p39d > 5 && g_p39e >= 1 && g_p39f >= 1? "Z23":"-";
		 
		 //项目40-PF2
		 double g_p40a = Fx.Match_Condition(3, "s4");
		 String p40b = Fx.Get_SF("f5", 0);
		 double g_p40c = Oz.Match_Condition(20, "nw<pw");
		 double g_p40d = Yz.Match_Condition(12, "pp=-0.25&np=-0.5");
		 double g_p40e = Yz.Match_Condition(12, "1.000<nlw");
		 String r_40 = g_p40a == 3 && p40b.equals("负") && g_p40c > 12 && g_p40d > 5 && g_p40e > 5? "PF2":"-";
		 
		 //项目41-PG
		 double g_p41a = AveO10[0];
		 double g_p41b = AveO10[2];
		 double g_p41c = Oz.Match_Condition(10, "3.50<nd");
		 String r_41 = g_p41a > 2.00 && g_p41b > 2.00 && g_p41c >= 2? "PG":"-";
		 
		 //项目42-PG2
		 double g_p42a = AveO10[0];
		 double g_p42b = AveO10[2];
		 double g_p42c = Oz.Match_Condition(10, "3.80<=nd");
		 String r_42 = g_p42a > 1.70 && g_p42b > 2.00 && g_p42c >= 2? "PG2":"-";
		 
		 //项目43-KS
		 double g_p43a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 double g_p43b = Fx.Match_Condition(2, "p5") + Fx.Match_Condition(2, "f5");
		 double g_p43c = Oz.Match_Condition(20, "nw<pw");
		 double g_p43d = Oz.Company_state("威廉希尔", "nd");
		 double g_p43e = Yz.Match_Condition(10, "np=-0.25");
		 double g_p43f = Yz.Match_Condition(10, "1.005<nlw");
		 String r_43 = g_p43a == 2 && g_p43b == 2 && g_p43c > 10 && g_p43d > 3.30 && g_p43e >= 5 && g_p43f >= 5? "KS":"-";
		 
		 //项目44-SNAI
		 double g_p44a;
		 int a = 0,b = 0;
		 double maxf = Oz.Max_Min(30, "nl")[0];
		 double maxs = Oz.Max_Min(30, "nw")[0];
		 if(maxf == Oz.Company_state("SNAI", "nl"))
			 a = 1;
		 if(maxs == Oz.Company_state("SNAI", "nw"))
			 b = 1;
		 g_p44a = b*2+a;
		 String r_44 = g_p44a > 0? "SNAI":"-";
		 
		 //项目45-WL
		 double g_p45a;
		 a = 0;b = 0;
		 if(maxf == Oz.Company_state("威廉希尔", "nl"))
			 a = 1;
		 if(maxs == Oz.Company_state("威廉希尔", "nw"))
			 b = 1;
		 g_p45a = b*2+a;
		 String r_45 = g_p45a > 0? "WL":"-";
		 
		 //项目46-Oddset
		 double g_p46a;
		 a = 0;b = 0;
		 if(maxf == Oz.Company_state("Oddset (奥德赛特)", "nl"))
			 a = 1;
		 if(maxs == Oz.Company_state("Oddset (奥德赛特)", "nw"))
			 b = 1;
		 g_p46a = b*2+a;
		 String r_46 = g_p46a > 0? "Oddset":"-";
		 
		 //项目47-Z12
		 double g_p47a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 String g_p47b = Fx.Get_SF("s5", 0);
		 double g_p47c = Fx.Series(5, "f1");
		 double g_p47d = Yz.Match_Condition(10, "np=-1 or np=-1.25");
		 double g_p47e = Oz.Match_Condition(10, "nw<pw");
		 String r_47 = g_p47a == 2 && g_p47b.equals("胜") && g_p47c >= 2 && g_p47d > 4 && g_p47e > 5? "Z12":"-";
		 
		 //项目48-Z24
		 String p48a = Fx.Get_SF("f4", 0);
		 String p48b = Fx.Get_SF("f5", 0);
		 double g_p48c = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p48d = Y[9];
		 String r_48 = p48a.equals("负") && p48b.equals("负") && g_p48c < 1 && g_p48d > 5? "Z24":"-";
		 
		 //项目49-Z25
		 String p49a = Fx.Get_SF("f4", 0);
		 String p49b = Fx.Get_SF("p5", 0);
		 double g_p49c = Oz.Match_Condition(10, "nl<pl");
		 double g_p49d = Yz.Match_Condition(10, "1.005<nrw");
		 double g_p49e = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 double g_p49f1 = Oz.Company_state("威廉希尔", "nd");
		 double g_p49f2 = Oz.Company_state("立博", "nd");
		 double g_p49f = g_p49f1 != 0 && g_p49f2 != 0? g_p49f1 - g_p49f2 : -1;
		 String r_49 = p49a.equals("负") && p49b.equals("平") && g_p49c > 5 && g_p49d > 4 && g_p49e > 4 && g_p49f > 0? "Z25":"-";
		 
		 //项目50-PF9
		 double g_p50a = Fx.Match_Condition(2, "s4");
		 double g_p50b = Yz.Match_Condition(10, "pp<np&1.005<nlw&1.005<plw");
		 double g_p50c = Oz.Match_Condition(10, "pw<nw");
		 String r_50 = g_p50a == 2 && g_p50b >= 2 && g_p50c > 5? "PF9":"-";
		 
		 //项目51-2QXPSP
		 double g_p51a = Yz.Match_Condition(10, "np<=-1.5");
		 double g_p51b = Dx.Match_Condition(10, "np<=2.5");
		 double g_p51c = Fx.Match_Condition(4, "d4");
		 String r_51 = g_p51a > 6 && g_p51b > 7 && g_p51c >= 2? "2QXPSP":"-";
		 
		 //项目53-ZP4
		 double g_p53a = Oz.Match_Condition(10, "nw<pw");
		 double g_p53b = Yz.Match_Condition(10, "np<pp&1.005<nlw&1.005<plw");
		 String p53c = Fx.Get_SF("s1", 0);
		 String p53d = Fx.Get_SF("s4", 0);
		 String p53e = Fx.Get_SF("f5", 0);
		 String r_53 = g_p53a >= 5 && g_p53b > 3 && p53c.equals("胜") && p53d.equals("胜") && p53e.equals("负")? "ZP4":"-";
		 
		 //项目54-ZP20
		 String p54a = Fx.Get_SF("f4", 0);
		 String p54b = Fx.Get_SF("s5", 0);
		 double g_p54c = Yz.Match_Condition(10, "1.005<nrw");
		 double g_p54d = Y[10];
		 String r_54 = p54a.equals("负") && p54b.equals("胜") && g_p54c > 5 && g_p54d > 5? "ZP20":"-";
		 
		 //项目56-ZP24(QDXP)
		 String g_p56a = Fx.Get_SF("s4", 0);
		 String g_p56b = Fx.Get_SF("s5", 0);
		 double g_p56c = Fx.Get_Rank("z");
		 double g_p56d = Oz.Match_Condition(10, "nw<pw");
		 double g_p56e = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p56f = Y[8];
		 String r_56 = g_p56a.equals("胜") && (g_p56b.equals("负") | g_p56b.equals("平")) && g_p56c <= 2 && g_p56c > 0 && g_p56d > 5 && 
				 g_p56e >= 1 && g_p56f >= 1? "ZP24(QDXP)":"-";
		 
		 //项目57-Z17
		 double g57a1 = Fx.Get_Rank("t");
		 double g57a2 = Fx.Get_Rank("z");
		 double g_p57a = (g57a1 == 0 | g57a2 == 0)? -1 : g57a1 - g57a2 + 1;
		 double g_p57b = Yz.Match_Condition(10, "pp=-0.25&plw<0.9&np=-0.75&1.005<nlw");
		 String r_57 = g_p57a > 0 && g_p57a < 4 && g_p57b > 2? "Z17":"-";
				 
		 //项目58-ZP5
		 double g_p58a = Fx.Match_Condition((int)F[0], "f1");
		 double g_p58b = g_p53a;
		 double g_p58c = Oz.Match_Condition(10, "3.5<=nd");
		 double g_p58d = Yz.Match_Condition(12, "pp=-0.25&np=-0.5");
		 double g_p58e = Fx.Match_Condition(4, "f5");
		 String p58f = Fx.Get_SF("f5", 0);
		 String p58g = Fx.Get_SF("s4", 0);
		 String r_58 = g_p58a == 0 && F[0] > 2 && g_p58b >= 6 && g_p58c >= 1 && g_p58d > 4 && g_p58e > 1 && 
				 p58f.equals("负") && p58g.equals("胜")? "ZP5":"-";
		 
		 //项目62-Z18
		 double g_p62a = Fx.Match_Condition(3, "p5") + Fx.Match_Condition(3, "s5");
		 double g62b1 = Fx.Get_Rank("t");
		 double g62b2 = Fx.Get_Rank("z");
		 double g_p62b = (g62b1 == 0 | g62b2 == 0)? -1 : g62b1 - g62b2 + 1;
		 double g_p62c = Yz.Match_Condition(10, "pp=-0.5&1.005<plw&np=-0.75&1.005<nlw");
		 String r_62 = g_p62a == 3 && g_p62b > 0 && g_p62b < 4 && g_p62c > 2? "Z18":"-";
		 
		 //项目63-SF
		 double g_p63a = Fx.Match_Condition((int)F[0], "p1");
		 double g_p63b = Oz.Company_state("威廉希尔", "pd") - Oz.Company_state("威廉希尔", "nd");
		 double g_p63c = Oz.Company_state("威廉希尔", "nd");
		 double g_p63d = Oz.Company_state("威廉希尔", "nl");
		 String r_63 = g_p63a >= 3 && g_p63b > 0.3 && g_p63c > 2.90 && g_p63c < 3.60 && g_p63d > 1.80? "SF":"-";
		 
		 //项目64-QDSP
		 double g_p64a = Y[3];
		 double g_p64b = D[2];
		 String r_64 = g_p64a > 4 && g_p64b > 4? "QDSP":"-";
		 
		 //项目65-ZP6
		 double g_p65a = Fx.Get_Rank("zd");
		 double g_p65b = Yz.Match_Condition(10, "np=-0.5");
		 double g_p65c = Oz.Match_Condition(10, "nd=nl");
		 String r_65 = g_p65a == 1 && g_p65b > 3 && g_p65c == 0? "ZP6":"-";
		 
		 //项目68-QDBS2
		 double g_p68a = Fx.Match_Condition(4, "s2");
		 double g_p68b = Fx.Match_Condition(4, "f3");
		 double g_p68c = Yz.Match_Condition(10, "pp=-1&plw<0.80");
		 double g_p68d = Yz.Match_Condition(10, "pp=-1.25&1.005<plw");
		 String p68e = Fx.Get_SF("s1", 0);
		 String r_68 = g_p68a >= 3 && g_p68b >= 3 && g_p68c >= 2 && g_p68d >= 2 && p68e.equals("胜")? "QDBS2":"-";
		 
		 //项目70-ZP13
		 double g_p70a = Oz.Match_Condition(20, "nd=nl&3.29<nd&3.29<nl");
		 double g_p70b = Fx.Match_Condition(3, "s5");
		 double g_p70c = Fx.Match_Condition(4, "s3");
		 String p70d = Fx.Get_SF("s3", 0);
		 double g_p70e = Fx.Match_Condition(3, "f4");
		 double g_p70f = Fx.Match_Condition(4, "f2");
		 String p70g = Fx.Get_SF("s2", 0); //负
		 String p70h = Fx.Get_SF("s4", 0);
		 String p70i = Fx.Get_SF("s5", 0);
		 String r_70 = g_p70a >= 2 && g_p70b >= 1 && g_p70c >= 2 && p70d.equals("胜") && g_p70e >= 1 &
				 g_p70f >= 2 && p70g.equals("负") && p70h.equals("负") && p70i.equals("胜")? "ZP13":"-";
		 
		 //项目71-PF5
		 double g_p71a = AveO10[0];
		 double g_p71b = AveO10[2];
		 double g_p71c = g_p41c;
		 double g_p71d = Oz.Company_state("威廉希尔", "nd");
		 double g_p71e = g_p19a;
		 double g_p71f = Yz.Match_Condition(10, "1.005<nrw");
		 String p71g = Fx.Get_SF("f5", 0);
		 double g_p71h = Oz.Match_Condition(10, "nl<pl&pd<nd");
		 String r_71 = g_p71a > 2.00 && g_p71a < 4.60 && g_p71b >= 1.60 && g_p71b <= 2.10 && g_p71c >= 2 &
				 g_p71d >= 3.49 && g_p71d <= 3.81 && g_p71e == F[0] && F[0] > 2 && g_p71f > 3 && p71g.equals("胜") && g_p71h > 4? "PF5":"-";
		 
		 //项目72-D1
		 double g_p72a = g_p17d;
		 double g_p72b = Dx.Match_Condition(15, "np<=2.5&1.005<nx");
		 String r_72 = g_p72a == F[0] && F[0] > 2 && g_p72b > 6? "D1":"-";
		 
		 //项目73-D2
		 double g_p73a = Fx.Series(10, "x4");
		 double g_p73b = Fx.Series(10, "x5");
		 String p73c = Fx.Get_SF("x4", 0);
		 String p73d = Fx.Get_SF("x5", 0);
		 double g_p73e = Dx.Match_Condition(20, "1.00<px");
		 String r_73 = (g_p73a > 4 | g_p73b > 4) && p73c.equals("小") && 
				 p73d.equals("小") && g_p73e > 10? "D2":"-";
		 
		 //项目76-PF6
		 double g_p76a = AveO10[2];
		 double g_p76b = O[9];
		 double g_p76c = Fx.Match_Condition(3, "f3");
		 String p76d = Fx.Get_SF("s2", 0);
		 String p76e = Fx.Get_SF("s4", 0);
		 double g_p76f = 5-Fx.Match_Condition(5, "s3");
		 String r_76 = g_p76a >= 1.75 && g_p76a <= 2.00 && g_p76b > 0.6 && g_p76c >= 1 &
				 p76d.equals("胜") && p76e.equals("胜") && g_p76f >= 3? "PF6":"-"; 
			
		 //项目80-YQXP7
		 double g_p80a = Fx.Get_Rank("kd");
		 double g_p80b = Yz.Match_Condition(10, "np=-1&1.005<nlw");
		 String r_80 = g_p80a == 1 && g_p80b > 2? "YQXP7":"-";
		 
		 //项目81-ZP18
		 double g_p81a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 double g_p81b = Fx.Match_Condition(2, "s5");
		 double g_p81c = Yz.Company_state("澳门", "np");
		 double g_p81d = Yz.Company_state("澳门", "nlw");
		 String r_81 = g_p81a == 2 && g_p81b == 2 && g_p81c <= 0 && g_p81c >= -0.5 && g_p81d > 0.975? "ZP18":"-";
		 
		 //项目82-FS5
		 double g_p82a = Fx.Series(10, "s5");;
		 double g_p82b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 double g_p82c = g_p58c;
		 String r_82 = g_p82a > 3 && g_p82b > 4 && g_p82c >= 2? "FS5":"-";
		 
		 //项目83-FS6
		 double g_p83a = g_p19a;
		 double g_p83b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 double g_p83c = Oz.Match_Condition(10, "3.30<=nl");
		 String r_83 = g_p83a == F[0] && F[0] > 2 && g_p83b > 4 && g_p83c >= 8? "FS6":"-";
		 
		 //项目85-ZP10
		 double g_p85a = g_p70e;
		 double g_p85b = Fx.Match_Condition(3, "s5");
		 double g_p85c = Oz.Match_Condition(10, "nw<pw&pd<nd&pl<nl");
		 double g_p85d = Y[5];
		 double g_p85e = Yz.Match_Condition(12, "1.005<nlw");
		 String r_85 = g_p85a >= 2 && g_p85b >= 2 && g_p85c > 6 && g_p85d > 5 && g_p85e >= 2? "ZP10":"-";
		 
		 //项目86-ZP3K
		 double g_p86a = Yz.Company_state("Interwetten", "np");
		 double g_p86b = Yz.Company_state("Interwetten", "nrw");
		 double g_p86c = Oz.Company_state("Interwetten", "nd");
		 double temp86a = Oz.Company_state("威廉希尔", "nd");
		 double temp86b = Oz.Company_state("威廉希尔", "pd");
		 double g_p86d = temp86a > 0 && temp86b > 0? temp86a - temp86b : 10;
		 double g_p86e = Oz.Company_state("立博", "nd");
		 String r_86 = g_p86a == 0 && g_p86b >= 1.3 && g_p86c >= 3.40 && g_p86d <= -0.2 && g_p86e >= 3.50? "ZP3K":"-";
		 
		 //项目87-S4
		 double g_p87a = Dx.Match_Condition(10, "np<2.75&1.40<nd");
		 double g_p87b = Fx.Match_Condition(4, "d4");
		 double g_p87c = Fx.GP_Condition(4, "zg4=0&kg4=0");
		 double g_p87d = Fx.Match_Condition(4, "d5");
		 double g_p87e = Fx.GP_Condition(4, "zg5=0&kg5=0");
		 double g_p87f = Fx.Match_Condition((int)F[0], "d1");
		 String r_87 = g_p87a >= 2 && g_p87b >= 1 && g_p87c == 0 && g_p87d >= 1 && g_p87e == 0 && g_p87f == F[0] && F[0] != 0? "S4":"-";
		 
		 //项目88-S5
		 double g_p88a = Dx.Match_Condition(20, "np=0.5");
		 String r_88 = g_p88a >= 1? "S5":"-";
		 
		 //项目89-KP2
		 double g_p89a = AveO10[2];
		 double g_p89b = AveO10[0];
		 double g_p89c = g_p41c;
		 double g_p89d = Oz.Company_state("威廉希尔", "nd");
		 double g_p89e = F[0]-Fx.Match_Condition((int)F[0], "f1");
		 double g_p89f = Yz.Match_Condition(10, "1.005<nlw");
		 String p89g = Fx.Get_SF("f4", 0);
		 double g_p89h = Oz.Match_Condition(10, "nw<pw&pd<nd");
		 String r_89 = g_p89a > 2.00 && g_p89b >= 1.60 && g_p89b <= 2.10 && g_p89c >= 2 && g_p89d >= 3.49 && g_p89d <= 3.81 && 
				 g_p89e == F[0] && F[0] > 2 && g_p89f > 3 && p89g.equals("胜") && g_p89h > 4? "KP2":"-";
		 
		 //项目90-PF21
		 double g_p90a = Fx.Series(10, "s4");
		 double g_p90b = Fx.Series(10, "f5");
		 double g_p90c = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 String r_90 = g_p90a >= 2 && g_p90b >= 2 && g_p90c > 2? "PF21":"-";
				 
		 //项目91-ZP14
		 String p91a = Fx.Get_SF("f4", 0);
		 String p91b = Fx.Get_SF("f5", 0);
		 double g_p91c = AveO10[0];
		 double g_p91d = AveO10[2];
		 double g_p91e = Oz.Match_Condition(10, "3.50<nd");
		 double g_p91f = Yz.Match_Condition(10, "pp=0.25&np=0.5");
		 String r_91 = p91a.equals("平") && p91b.equals("平") && g_p91c > 2.00 && g_p91d >= 1.60 && g_p91d <= 2.10 &
				 g_p91e > 2 && g_p91f > 4? "ZP14":"-";
		 
		 //项目92-ZP21
		 double g_p92a = Fx.Series(10, "f4");
		 double g_p92b = Fx.Series(10, "s5");
		 double g_p92c = Yz.Match_Condition(10, "np<-0.25&-10<np");
		 String r_92 = g_p92a >= 2 && g_p92b >= 2 && g_p92c > 4? "ZP21":"-";
				
		 //项目93-ZP22
		 double g_p93a = Fx.Series(10, "f4");
		 double g_p93b = Fx.Series(10, "s5");
		 double g_p93c = Yz.Match_Condition(10, "np=-0.25&1.005<nrw");
		 String r_93 = g_p93a >= 2 && g_p93b >= 2 && g_p93c > 4? "ZP22":"-";
		 
		 //项目94-ZP23
		 double g_p94a = Fx.Series(10, "s4");
		 double g_p94b = Fx.Series(10, "f5");
		 double g_p94c = Yz.Match_Condition(10, "0.25<np&np<1&1.005<nrw");
		 String r_94 = g_p94a >= 2 && g_p94b >= 2 && g_p94c > 2? "ZP23":"-";
		 
		 //项目96-SF6
		 double g_p96a = Fx.Match_Condition(4, "p4") + Fx.Match_Condition(4, "f4");
		 double g_p96b = Fx.Match_Condition(4, "p5") + Fx.Match_Condition(4, "s5");
		 double g_p96c = Fx.Match_Condition(4, "s5");
		 double g_p96d = Yz.Match_Condition(10, "-0.5<np&np<0.25&1.005<nrw");
		 String r_96 = g_p96a == 4 && g_p96b == 4 && g_p96c > 1 && g_p96d > 4? "SF6":"-";
		 
	     //项目97-ZP1
		 double g_p97a = Yz.Match_Condition(10, "np<pp&1.005<=plw&1.005<=nlw");
		 double g_p97b = 4 - Fx.Match_Condition(4, "s5");
		 double g_p97c = Fx.Match_Condition(4, "f5");
		 double g_p97d = Fx.Match_Condition(4, "s4");
		 String r_97 = g_p97a >= 2 && g_p97b == 4 && g_p97c >= 2 && g_p97d >= 2? "ZP1":"-";
		 		 
		 //项目99-BL
		 double temp99a = Oz.Company_state("威廉希尔", "nd");
		 double temp99b = Oz.Company_state("立博", "nd");
		 double g_p99a = temp99a > 0 && temp99b > 0? temp99a - temp99b : -10;
		 double g_p99b = Yz.Match_Condition(10, "1.005<nlw");
		 String r_99 = g_p99a > 0.1 && g_p99b >= 1? "BL":"-";
		 
		 //项目100-ZP3K1
		 double g_p100a = Yz.Company_state("Interwetten", "np");
		 double g_p100b = Yz.Company_state("Interwetten", "nrw");
		 double g_p100c = Oz.Company_state("Interwetten", "nd");
		 double temp100a = Oz.Company_state("威廉希尔", "nd");
		 double temp100b = Oz.Company_state("立博", "nd");
		 double g_p100d = temp100a > 0 && temp100b > 0? temp100a - temp100b : -10;
		 double g_p100e = Yz.Match_Condition(10, "1.005<nlw");
		 String r_100 = g_p100a == 0 && g_p100b >= 1.3 && g_p100c >= 3.40 && g_p100d > 0 && g_p100e > 2? "ZP3K1":"-";
		 
		 //项目101-FS1
		 double g_p101a = 2-Fx.Match_Condition(2, "s4");
		 double g_p101b = Fx.Match_Condition(2, "s5");
		 double g_p101c = Yz.Match_Condition(10, "pp=-0.25&np=-0.5");
		 double g_p101d = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p101e = Oz.Match_Condition(10, "3.50<nd");
		 String r_101 = g_p101a == 2 && g_p101b == 2 && g_p101c > 3 && g_p101d > 2 && g_p101e > 4? "FS1":"-";

		 //项目104-ZP18ZP2K
		 double g_p104a = Fx.Match_Condition(2, "p4") + Fx.Match_Condition(2, "f4");
		 double g_p104b = Fx.Match_Condition(2, "s4");
		 double g_p104c = Yz.Company_state("澳门", "np");
		 double g_p104d = Yz.Company_state("澳门", "nlw");
		 double g_p104e = Fx.Match_Condition(3, "s2") + Fx.Match_Condition(3, "p2");
		 double g_p104f = Fx.Match_Condition(3, "s2");
		 double g_p104g = Yz.Match_Condition(10, "nrw<0.8");
		 String r_104 = g_p104a == 2 && g_p104b == 2 && g_p104c <= 0 && g_p104c >= -0.5 && g_p104d > 0.975 && g_p104e == 3 && g_p104f > 1 && g_p104g >= 1? "ZP18ZP2K":"-";
		
		 //项目105-ZP26
		 double g_p105a = Fx.Series(10, "s5");
		 double g_p105b = Fx.Series(10, "f4");
		 double g_p105c = Yz.Match_Condition(10, "np=0.5&1.005<nrw");
		 double g_p105d = Oz.Match_Condition(10, "nl<pl");
		 String r_105 = g_p105a >= 2 && g_p105b >= 2 && g_p105c >= 4 && g_p105d > 5? "ZP26":"-";
		 
		 //项目106-P6
		 String p106a = Fx.Get_SF("s4", 0);
		 String p106b = Fx.Get_SF("s5", 0);
		 double g_p106c = O[15];
		 double g_p106d = O[16];
		 double g_p106e = Oz.Match_Condition(10, "nd<=3.30");
		 double g_p106f = Dx.Match_Condition(10, "1.00<nd");
		 String r_106 = p106a.equals("负") && p106b.equals("胜") && g_p106c > 2 && g_p106d > 5 && g_p106e > 6 && g_p106f > 5? "P6":"-";
		 
		 //项目114-QDBS7
		 double g_p114a = Fx.Series(10, "s4");
		 double g_p114b = Fx.Series(10, "f5");
		 double g_p114c = Fx.Match_Condition((int)F[0], "s1");
		 double g_p114d = Yz.Match_Condition(10, "np=-1&1.005<nlw");
		 double g_p114e = Oz.Match_Condition(10, "nw<pw");
		 String r_114 = g_p114a >= 4 && g_p114b >= 3 && g_p114c > 2 && g_p114d >= 2 && g_p114e > 6? "QDBS7":"-";
		 
		 //项目115-YK1
		 double g_p115a = Yz.Match_Condition(10, "pp<np&nrw<prw");
		 double g_p115b = Yz.Match_Condition(10, "1.005<nrw");
		 String p115c = Fx.Get_SF("s5", 0);
		 double g_p115d = Oz.Match_Condition(10, "nl<pl");
		 String r_115 = g_p115a > 5 && g_p115b >= 2 && p115c.equals("胜") && g_p115d > 6? "YK1":"-";
		 
		 //项目117-Z2
		 double g_p117a = Fx.Match_Condition((int)F[0], "s1");
		 double g_p117b = Fx.Match_Condition(5, "s2");
		 double g_p117c = Oz.Match_Condition(10, "pw<nw&nd<pd&nl<pl");
		 double g_p117d = Oz.Match_Condition(10, "3.60<=nd");
		 double g_p117e = Yz.Match_Condition(10, "pp<np&1.005<nlw&1.005<plw");
		 String p117f = Fx.Get_SF("s4", 0);
		 String p117g = Fx.Get_SF("s1", 0);
		 String r_117 = g_p117a > 3 && g_p117b > 3 && g_p117c > 5 && g_p117d > 2 && g_p117e >= 1 && 
				 (p117f.equals("负")|p117g.equals("负"))? "Z2":"-";
		 
		 //项目126-ZP19
		 double g_p126a = Yz.Match_Condition(10, "pp=-0.75&np<=-1");
		 double g_p126b = Oz.Match_Condition(10, "nl<=5.0");
		 double g_p126c = AveO10[1];
		 String p126d = Fx.Get_SF("s4", 0);
		 String r_126 = g_p126a > 4 && g_p126b >= 3 && g_p126c > 4.0 && p126d.equals("胜")? "ZP19":"-";
		 
		 //项目128-Z8
		 double g_p128a = Yz.Match_Condition(10, "pp=-0.75&np=-1");
		 double g_p128b = Oz.Match_Condition(10, "nl<=5.0");
		 String p128c = Fx.Get_SF("s5", 0);
		 String p128d = Fx.Get_SF("s4", 0);
		 String p128e = Fx.Get_SF("s1", 0);
		 String r_128 = g_p128a > 4 && g_p128b >= 3 && p128c.equals("负") && p128d.equals("胜") && 
				 p128e.equals("胜")? "Z8":"-";
		 
		 //项目130-F2
		 double g_p130a = 6 - Fx.Match_Condition(6, "f2");
		 String p130b = Fx.Get_SF("s1", 0);
		 double g_p130c = Yz.Match_Condition(10, "np=-0.25&1.005<nlw");
		 double g_p130d = Oz.Match_Condition(10, "3.40<nd");
		 String r_130 = g_p130a == 6 && (p130b.equals("胜")|p130b.equals("平")) && g_p130c >= 4 && g_p130d >= 5? "F2":"-";
		 
		 //项目133-F4
		 double g_p133a = Fx.Match_Condition(6, "s4");
		 double g_p133b = Fx.Match_Condition(6, "s5");
		 double g_p133c = Fx.Match_Condition(3, "p1") + Fx.Match_Condition(3, "f1");
		 double g_p133d = Oz.Company_state("威廉希尔", "nw");
		 double g_p133e = Oz.Company_state("威廉希尔", "nd");
		 double g_p133f = Oz.Company_state("Ladbrokes (立博)", "nw");
		 double g_p133g = Oz.Company_state("Ladbrokes (立博)", "nd");
		 String r_133 = g_p133a >= 4 && g_p133b >= 4 && g_p133c > 1 && ((g_p133d>=1.80&g_p133e>3.60)|(g_p133d>=1.75&g_p133e>3.80)) && 
				 ((g_p133f>=1.80&g_p133g>3.60)|(g_p133f>=1.75&g_p133g>3.80))? "F4":"-";
		 
		 //项目137-F5
		 double g_p137a = 5 - Fx.Match_Condition(5, "s4");
		 double g_p137b = 5 - Fx.Match_Condition(5, "s5");
		 double g_p137c = Oz.Match_Condition(10, "pd=pl&pd>=3.30&pl>=3.30");
		 double g_p137d = Yz.Match_Condition(10, "np=-0.5&1.005<nlw");
		 String r_137 = g_p137a >= 4 && g_p137b >= 4 && g_p137c >= 2 && g_p137d > 4? "F5":"-";
		 
		 //项目138-PF13
		 double g_p138a = 5 - Fx.Match_Condition(5, "s4");
		 double g_p138b = 5 - Fx.Match_Condition(5, "f5");
		 double g_p138c = Fx.Match_Condition(5, "s5");
		 double g_p138d = Oz.Match_Condition(10, "nd=nw&3.40<=nd&3.40<=nw");
		 double g_p138e = Yz.Match_Condition(10, "1.005<nlw");
		 String r_138 = g_p138a >= 4 && g_p138b >= 4 && g_p138c >= 3 && g_p138d >= 1 && g_p138e > 4? "PF13":"-";

		 //项目141-Z7
		 String p141a = Fx.Get_SF("s1", 0); 
		 String p141b = Fx.Get_SF("s5", 0); 
		 double g_p141c = Oz.Match_Condition(10, "pw<pl");
		 double g_p141d = Oz.Match_Condition(10, "nl<nw");
		 double g_p141e = Yz.Match_Condition(10, "1.005<plw");
		 double g_p141f = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p141g = Yz.Match_Condition(20, "np=0.25&1.005<nrw");
		 double g_p141h = Oz.Match_Condition(10, "3.40<=nd");
		 String r_141 = (p141a.equals("平")|p141a.equals("负")) && p141b.equals("胜") && g_p141c > 8 && g_p141d > 6 && g_p141e > 5 && g_p141f > 5 &
				 g_p141g >= 2 && g_p141h >= 3? "Z7":"-";
		 
		 //项目145-Z
		 double g_p145a = Fx.Series(10, "s2");
		 String p145b = Fx.Get_SF("s1", 0);
		 String p145c = Fx.Get_SF("s5", 0);
		 double g_p145d = Oz.Match_Condition(10, "pw<nw");
		 double g_p145e = O[14];
		 double g_p145f = Yz.Match_Condition(10, "1.005<nlw");
		 String r_145 = g_p145a >= 3 && (p145b.equals("胜")|p145b.equals("平")) && (p145c.equals("平")|p145c.equals("负")) && 
				 g_p145d > 5 && g_p145e >= 4 && g_p145f >= 2? "Z":"-";		 

		 //项目147-P3
		 double g_p147a = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p147b = Oz.Match_Condition(10, "nw=nl");
		 double g_p147c = Oz.Match_Condition(10, "pl<pw&nw<nl");
		 double g_p147d = Oz.Match_Condition(10, "nd<3.30");
		 String r_147 = g_p147a > 3 && g_p147b >= 1 && g_p147c > 6 && g_p147d > 8? "P3":"-";
		 
		 //项目149-ZP16
		 double g_p149a = Oz.Match_Condition(10, "nw<pw");
		 double g_p149b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 double g_p149c = Oz.Match_Condition(10, "nd<3.30");
		 String r_149 = g_p149a > 6 && g_p149b > 4 && g_p149c > 8? "ZP16":"-";
		 
		 //项目151-BF
		 double g_p151a = Dx.Match_Condition(10, "3.00<nd or 3.00<nx");
		 String r_151 = g_p151a > 0? "BF":"-";
		 
		 //项目153-ZP11
		 double g_p153a = Fx.Match_Condition(10, "s3");
		 double g_p153b = Oz.Match_Condition(10, "pl<pd");
		 double g_p153c = Oz.Match_Condition(10, "nd<nl");
		 double g_p153d = Oz.Match_Condition(10, "3.40<=nd");
		 double g_p153e = Yz.Match_Condition(10, "nlw<1.005");
		 String r_153 = g_p153a <= 1 && g_p153b > 8 && g_p153c > 6 && g_p153d > 4 && g_p153e > 8? "ZP11":"-";
		 
		 //项目154-QDXP5
		 double g_p154a = Yz.Match_Condition(10, "1.005<nlw");
		 double g_p154b = Dx.Match_Condition(10, "pp<np&nd<pd");
		 String r_154 = g_p154a > 5 && g_p154b > 2? "QDXP5":"-";

		 //项目160-Z10
		 double g_p160a = 10 - Fx.Match_Condition(10, "f3");
		 String p160b = Fx.Get_SF("s4", 0);
		 String p160c = Fx.Get_SF("s1", 0);
		 double g_p160d = Oz.Match_Condition(10, "nl=nd&3.20<nd&3.20<nl");
		 String r_160 = g_p160a == 10 && p160b.equals("负") && p160c.equals("负") && g_p160d >= 1? "Z10":"-";

		 //项目163-PF7
		 double g_p163a = 10 - Fx.Match_Condition(10, "s3");
		 double g_p163b = Oz.Match_Condition(10, "nl<nd");
		 double g_p163c = Oz.Match_Condition(10, "nw<pw");
		 double g_p163d = AveO10[0];
		 double g_p163e = Oz.Match_Condition(10, "3.50<nd");
		 String r_163 = g_p163a == 10 && g_p163b >= 4 && g_p163c > 8 && g_p163d > 1.80 && g_p163e > 5? "PF7":"-";

		 //项目167-SF4
		 double g_p167a = Fx.Match_Condition(2, "f1");
		 double g_p167b = Fx.Match_Condition(2, "f4");
		 double g_p167c = 2 - Fx.Match_Condition(2, "s5");
		 double g_p167d = 10 - Fx.Match_Condition(10, "f2");
		 double g_p167e = Oz.Match_Condition(10, "nl<pl");
		 double g_p167f = Oz.Match_Condition(10, "nd<=3.20");
		 double g_p167g = Yz.Match_Condition(10, "1.005<nrw");
		 String r_167 = g_p167a == 2 && g_p167b == 2 && g_p167c == 2 && g_p167d > 6 && g_p167e > 7 && g_p167f > 8 && g_p167g < 3? "SF4":"-";
		 
		 //项目169-QDBS9
		 double g_p169a = Yz.Match_Condition(10, "pp=-0.75&np=-1");
		 double g_p169b = Oz.Match_Condition(10, "nl<=5.0");
		 double g_p169c = AveO10[1];
		 String p169d = Fx.Get_SF("s4", 0);
		 String r_169 = g_p169a > 4 && g_p169b >= 3 && g_p169c > 4.0 && p169d.equals("负")? "QDBS9":"-";

		 //项目179-F10
		 double g_p179a = Fx.Match_Condition(2, "s2");
		 double g_p179b = Fx.Match_Condition(2, "f3");
		 double g_p179c = 2 - Fx.Match_Condition(2, "f1");
		 double g_p179d = Yz.Match_Condition(10, "pp=-0.5&1.005<plw");
		 double g_p179e = Yz.Match_Condition(10, "np=-0.25&1.005<nlw");
		 double g_p179f = Oz.Match_Condition(10, "3.40<=nd");
		 double g_p179g = Oz.Match_Condition(10, "nd=nl");
		 String r_179 = g_p179a == 2 && g_p179b == 2 && g_p179c == 2 && g_p179d >= 3 && g_p179e >= 4 &
				 g_p179f >= 2 && g_p179g > 1? "F10":"-";
		 
		 //项目188-F7
		 double g_p188a = Fx.Series((int)F[0], "f1");
		 double g_p188b = Oz.Company_state("威廉希尔", "pw") == 0? 
				 0:100*(Oz.Company_state("威廉希尔", "pw")-Oz.Company_state("威廉希尔", "nw"))/Oz.Company_state("威廉希尔", "pw");
		 double g_p188c = Oz.Company_state("威廉希尔", "pl") == 0? 
				 0:100*(Oz.Company_state("威廉希尔", "nl")-Oz.Company_state("威廉希尔", "pl"))/Oz.Company_state("威廉希尔", "pl");
		 double g_p188d = Oz.Company_state("威廉希尔", "pd") == 0? 
				 0:100*(Oz.Company_state("威廉希尔", "nd")-Oz.Company_state("威廉希尔", "pd"))/Oz.Company_state("威廉希尔", "pd");
		 double g_p188e = Yz.Match_Condition(10, "prw<0.95");
		 double g_p188f = Yz.Match_Condition(10, "nrw<0.95");
		 String r_188 = g_p188a >= 2 && g_p188b > 20 && g_p188c > 20 && g_p188d > 0 && g_p188e > 8 && g_p188f > 8? "F7":"-";
		 
		 //项目189-D3
		 double g_p189a = Dx.Match_Condition(10, "pp=3.5&np=3.0&1.05<nd");
		 double g_p189b = Yz.Match_Condition(10, "1.005<nlw");
		 String r_189 = g_p189a >= 2 && g_p189b > 5? "D3":"-";
		 
		 if(Q.size() == 5)
			 Q.remove(0);
		 Q.add(Temp);
		 modify3("3Z1", DC);
		 modify3("3Z2", DC);
		 modify3("3K1", DC);
		 modify3("2P1", DC);
		 modify3("2P2", DC);
		 modify3("2Z1", DC);
		 
		 String colc = "";
 	     String[] r = {r_2,r_3,r_4,r_5,r_6,r_7,r_8,r_9,r_10,r_11,r_12,r_13,r_14,r_15,r_16,r_17,r_18,r_19,r_20,r_21,r_22,
 	    		r_23,r_25,r_26,r_28,r_29,r_30,r_31,r_32,r_33,r_34,r_35,r_36,r_37,r_39,r_40,
 	    		r_41,r_42,r_43,r_44,r_45,r_46,r_47,r_48,r_49,r_50,r_51,r_53,r_54,r_56,r_57,r_58,r_62,r_63,r_64,
 	    		r_65,r_68,r_70,r_71,r_72,r_73,r_76,r_80,r_81,r_82,r_83,r_85,r_86,r_87,r_88,r_89,r_90,r_91,r_92,
 	    		r_93,r_94,r_96,r_97,r_99,r_100,r_101,r_104,r_105,r_106,r_114,r_115,r_117,r_126,r_128,r_130,r_133,r_137,r_138,r_141,r_145,r_147,
 	    		r_149,r_151,r_153,r_154,r_160,r_163,r_167,r_169,r_179,
 	    		r_188,r_189};
  		 for(int i = 0 ; i < r.length ; i++){
  			 if(!r[i].equals("-")){
  				 colc = colc + r[i] + "、";
  			 }
  		 }
  		 if(colc.equals(""))
  			 colc = "-";
  		 else
  			 colc = colc.substring(0, colc.length()-1);
  		 //单场SP值 
  		 double w, d, l;
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
  			 //SP = SP.substring(1, SP.length()-1);
  			 //项目1-ZZ
  			 w = Lists.match_SP(id,SP)[0];
  			 d = Lists.match_SP(id,SP)[1];
  			 l = Lists.match_SP(id,SP)[2];
  		 }
		 if((w < 2.00 && w != 0)||(d < 2.00 && d != 0)||(l < 2.00 && l != 0)){
	 		 if(colc == "-") colc = "ZZ";
			 else colc += "、ZZ";}
		 //项目24-KS1
		 double g_p24a = Oz.Match_Condition(20, "pw<nw&nd<pd&pl<nl");
		 double g_p24b = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl");
		 double g_p24c = Yz.Match_Condition(10, "1.005<nrw");
		 double g_p24d = l;
		 if(g_p24a > 5 && g_p24b > 5 && g_p24c >= 2 && g_p24d > 4.00){
			 if(colc == "") colc = "KS1"; 
			 else colc += "、KS1";
		 }		 
		 
		 //项目59-KS2
		 double g_p59a = Oz.Match_Condition(20, "pw<nw&nd<pd&pl<nl");
		 double g_p59b = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl");
		 double g_p59c = Yz.Match_Condition(10, "1.005<nrw");
		 double g_p59d = l;
		 double g_p59e = Oz.Match_Condition(20, "nd=nl");
		 if(g_p59a > 5 && g_p59b > 5 && g_p59c >= 2 && g_p59d > 4.00 && g_p59e > 1){
			 if(colc == "") colc = "KS2"; 
			 else colc += "、KS2";
		 }
		 
		 //项目38-Z16
		 double g_p38a = w;
		 double g_p38b = Oz.Match_Condition(20, "pw<nw&nd<pd&nl<pl&nd=nl");
		 double g_p38c = Yz.Match_Condition(10, "1.005<nlw");
		 if(g_p38a < 2.0 && g_p38a > 0 && g_p38b > 1 && g_p38c > 2){
			 if(colc == "") colc = "Z16"; 
			 else colc += "、Z16";
		 }
		 //项目118-Z3
		 double g_p118a = w;
		 double g_p118b = Yz.Match_Condition(10, "1.005<nrw");
		 double g_p118c = Oz.Match_Condition(10, "nd=nw");
		 if(g_p118a > 3.60 && g_p118b > 5 && g_p118c >= 1){
		 	 if(colc == "-") colc = "Z3";
			 else colc += "、Z3";}
		 //项目124-K7
		 double	g_p124a = l;
		 double	g_p124b = Yz.Match_Condition(10, "1.005<nlw");
		 double	g_p124c = Oz.Match_Condition(10, "nd=nl");
		 if(g_p124a > 3.60 && g_p124b > 5 && g_p124c >= 1){
			 if(colc == "-") colc = "K7";
			 else colc += "、K7";}
		 
		 //项目127-Z11
		 double g_p127a = w;
		 double g_p127b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
	 	 double g_p127c = AveO10[2];
		 if(g_p127a < 1.80 && g_p127a > 0 && g_p127b >= 2 && g_p127c < 3.50){
			 if(colc == "-") colc = "Z11";
			 else colc += "、Z11";}
		 
		 //项目139-KDBS2
		 double g_p139a = Fx.Series(10, "s5");
		 double g_p139b = Oz.Match_Condition(10, "nl<pl");
		 double g_p139c = 3 - Fx.Match_Condition(3, "s4");
		 double g_p139d = Yz.Match_Condition(10, "1.005<nrw");
		 double g_p139e = Yz.Match_Condition(10, "prw<0.90");
		 if(g_p139a >= 2 && g_p139b > 5 && g_p139c == 3 && g_p139c > 0 && g_p139d > 3 && g_p139e > 7){
			 if(colc == "-") colc = "KDBS2";
			 else colc += "、KDBS2";}
		 //项目142-ZS2
		 double g_p142a = w;
		 double g_p142b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nlw");
		 String p142c = Fx.Get_SF("s5", 0); 
		 String p142d = Fx.Get_SF("s4", 0);
		 if(g_p142a < 1.80 && g_p142a > 0 && g_p142b >= 5 && (p142c.equals("平")|p142c.equals("负"))& p142d.equals("胜")){
			 if(colc == "-") colc = "ZS2";
			 else colc += "、ZS2";}
		 
		 //项目144-ZP15
		 double g_p144a = l;
		 double g_p144b = Yz.Match_Condition(10, "pp=-0.25&np=-0.5&1.005<nrw");
		 double g_p144c = AveO10[0]; 
		 if(g_p144a < 1.80 && g_p144a > 0 && g_p144b >= 2 && g_p144c < 3.80){
			 if(colc == "-") colc = "ZP15";
		 	 else colc += "、ZP15";}
		 
  		 String[] rowdata ={index,colc,team1,score1,score2,team2,comp,time,state,half};
  		 return rowdata;
	}
	
	public void modify3(String str, String DC)
	{
		if(!Common_Use.Satisfy(Q, str))
			return;
		int temp = str.charAt(0) - '0';
		for(int i = Q.size() - 2; i >= Q.size() - temp - 1; i--)
		{
			int z = (int) Common_Use.parsedata(Q.get(i).ZGOAL, -1);
	   		int k = (int) Common_Use.parsedata(Q.get(i).KGOAL, -1);
	   		if(Q.get(i).State.equals("未"))
	   			this.Data_Map.get(str).Not_Start.add(DC+"期第"+Q.get(i).ID+"场 - "+Q.get(i).Time+" - "+Q.get(i).Home+" vs "+Q.get(i).Visit);
	   		else if(Q.get(i).State.equals("进行中"))
	   			this.Data_Map.get(str).Running.add(DC+"期第"+Q.get(i).ID+"场 - "+Q.get(i).Time+" - "+Q.get(i).Home+" vs "+Q.get(i).Visit);
	   		else if(z > k && z != -1 && k != -1)
	   			this.Data_Map.get(str).Win.add(DC+"期第"+Q.get(i).ID+"场 - "+Q.get(i).Time+" - "+Q.get(i).Home+" " + z + " : " + k + " "+Q.get(i).Visit);
	   		else if(z == k && z != -1 && k != -1)
	   			this.Data_Map.get(str).Draw.add(DC+"期第"+Q.get(i).ID+"场 - "+Q.get(i).Time+" - "+Q.get(i).Home+" " + z + " : " + k + " "+Q.get(i).Visit);
	   		else if(z < k && z != -1 && k != -1)
	   			this.Data_Map.get(str).Lose.add(DC+"期第"+Q.get(i).ID+"场 - "+Q.get(i).Time+" - "+Q.get(i).Home+" " + z + " : " + k + " "+Q.get(i).Visit);
		}
	}
	
	public void UpData() throws IOException
	{
		Data_Map.clear();
		String nowstr0 = Lists.choice0.getSelectedItem();
		String nowstr1 = Lists.choice1.getSelectedItem();
		ArrayList<String> Index = new ArrayList<String>();
		String s1,s2,s3;
		String numstr1= nowstr0.substring(4, 6);
		String numstr2= nowstr0.substring(2, 4);
		String numstr3= nowstr0.substring(0, 2);
		int d0 = Integer.parseInt(numstr1);
		int m0 = Integer.parseInt(numstr2);
		int y0 = Integer.parseInt(numstr3);
		int nd = d0, nm = m0, ny = y0;
		while(true){
			if(nd < 10) s1 = "0"+Integer.toString(nd);
			else s1 = Integer.toString(nd);
			if(nm < 10) s2 = "0"+Integer.toString(nm);
			else s2 = Integer.toString(nm);
			if(ny < 10) s3 = "0"+Integer.toString(ny);
			else s3 = Integer.toString(ny);
			Index.add(s3 + s2 + s1);
			//System.out.println(s3+s2+s1);
			if((s3+s2+s1).equals(nowstr1))
				break;
			if(nd-1>0) nd--;
			else{
				nd = 20;
				if(nm-1>0) nm--;
				else{
					nm = 12;
					if(ny-1>=0) ny--;
					else ny = 99;
				}
			}
		}
		if(Lists.sta_over)
			return;
		for(JRadioButton R : RadioButton.RB)
		{
			ArrayList<String> win = new ArrayList<String>();
			ArrayList<String> draw = new ArrayList<String>();
			ArrayList<String> lose = new ArrayList<String>();
			ArrayList<String> not = new ArrayList<String>();
			ArrayList<String> run = new ArrayList<String>();
			Data D = new Data(win, draw, lose, not, run);
			this.Data_Map.put(R.getText(), D);
		}
		this.sum = 0;
		for(String DC : Index)
		{
			Q.clear();
			Q.add(new InfoStore("-","-","-","-","-","-","-","-"));
			if(Lists.sta_over)
				return;
			DataEntry(DC);
		}
	}

	public void DataEntry(String DC) throws IOException
	{
		try{
			CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet("http://live.500.com/zqdc.php?e=" + DC);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity, "GBK");
			Document doc = Jsoup.parse(html);
			
			Element masthead = doc.getElementById("table_match");
			if(masthead == null)
				return;
		   	Elements links = masthead.select("tr");
		   	int count = 0;
		   	for (Element str : links){
		   		if(Lists.sta_over)
		   		{
		   			System.out.println("Check");
					return;
		   		}
		   		if(!str.attr("parentid").equals(""))
		   		    continue;
		   		if(count == 0){
		   			count++;
		  			continue;
		  		}
		   		String[] rowdata = GetMsg(str,count,doc,DC);
		   		if(rowdata == null)
		   			continue;
		   		boolean isfind = false;
		   		String[] s = rowdata[1].split("、");
		   		for(JRadioButton R : RadioButton.RB){
		   			isfind = false;
		   			if(Lists.sta_over)
						return;
			   		for(String ss : s)
			   			if(R.getText().equals(ss))
			   			{
			   				isfind = true;
			   				break;
			   			}
			   		if(!isfind)
			   			continue;
			   		int z = !rowdata[3].equals("")? Integer.parseInt(rowdata[3]) : -1;
			   		int k = !rowdata[4].equals("")? Integer.parseInt(rowdata[4]) : -1;
			   		if(rowdata[8].equals("未"))
			   			this.Data_Map.get(R.getText()).Not_Start.add(DC+"期第"+rowdata[0]+"场 - "+rowdata[7]+" - "+rowdata[2]+" vs "+rowdata[5]);
			   		else if(rowdata[8].equals("进行中"))
			   			this.Data_Map.get(R.getText()).Running.add(DC+"期第"+rowdata[0]+"场 - "+rowdata[7]+" - "+rowdata[2]+" vs "+rowdata[5]);
			   		else if(z > k && z != -1 && k != -1)
			   			this.Data_Map.get(R.getText()).Win.add(DC+"期第"+rowdata[0]+"场 - "+rowdata[7]+" - "+rowdata[2]+" " + z + " : " + k + " "+rowdata[5]);
			   		else if(z == k && z != -1 && k != -1)
			   			this.Data_Map.get(R.getText()).Draw.add(DC+"期第"+rowdata[0]+"场 - "+rowdata[7]+" - "+rowdata[2]+" " + z + " : " + k + " "+rowdata[5]);
			   		else if(z < k && z != -1 && k != -1)
			   			this.Data_Map.get(R.getText()).Lose.add(DC+"期第"+rowdata[0]+"场 - "+rowdata[7]+" - "+rowdata[2]+" " + z + " : " + k + " "+rowdata[5]);
			   	}
		   		this.sum++;
		   	    count++;
		   	    Lists.datatext.setText("正在统计第"+DC+"期\n共统计"+this.sum+"场 ");
		   	    for(JRadioButton JRB : RadioButton.RB)
				{
					String Term = JRB.getText();
					int numm = this.Data_Map.get(Term).Win.size()+
							this.Data_Map.get(Term).Draw.size()+
							this.Data_Map.get(Term).Lose.size()+
							this.Data_Map.get(Term).Not_Start.size()+
							this.Data_Map.get(Term).Running.size();
					if(numm > 1)
						JRB.setEnabled(true);
					else 
						JRB.setEnabled(false);
				}
			}
		   	this.textarea.append("第"+DC+"期统计完成\n");
		   	this.textarea.append("本期统计"+(count-1)+"场\n");
		   	this.textarea.append("共统计"+this.sum+"场\n");
		   	this.textarea.append("***********************\n");
		}catch(IOException e){
			Lists.datatext.setText("数据统计网络连接错误！");
			DataEntry(DC);
			return;
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String Term = arg0.getActionCommand();
		int num = this.Data_Map.get(Term).Win.size()+
				this.Data_Map.get(Term).Draw.size()+
				this.Data_Map.get(Term).Lose.size()+
				this.Data_Map.get(Term).Not_Start.size()+
				this.Data_Map.get(Term).Running.size();
		setDataChart(Term, num);
		((DefaultTableModel)table2.getModel()).getDataVector().removeAllElements();
		String[] rowdata = new String[5];
		for(int i = 0 ; true ; i++)
		{
			rowdata[0] = i < this.Data_Map.get(Term).Win.size()? this.Data_Map.get(Term).Win.get(i):"-";
			rowdata[1] = i < this.Data_Map.get(Term).Draw.size()? this.Data_Map.get(Term).Draw.get(i):"-";
			rowdata[2] = i < this.Data_Map.get(Term).Lose.size()? this.Data_Map.get(Term).Lose.get(i):"-";
			rowdata[3] = i < this.Data_Map.get(Term).Not_Start.size()? this.Data_Map.get(Term).Not_Start.get(i):"-";
			rowdata[4] = i < this.Data_Map.get(Term).Running.size()? this.Data_Map.get(Term).Running.get(i):"-";
			if(rowdata[0].equals("-")&rowdata[1].equals("-")&rowdata[2].equals("-")&rowdata[3].equals("-")&rowdata[4].equals("-"))
				break;
			((DefaultTableModel)table2.getModel()).addRow(rowdata);
			DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		    r.setHorizontalAlignment(JLabel.LEFT);
		    table2.setDefaultRenderer(Object.class, r);
		}
	}
	public void setDataChart(String term, int num)
	{
		// 通过工程创建3D饼图
		JFreeChart pieChart = ChartFactory.createPieChart("结论"+term+"（共"+this.sum+"场，其中"+num+"场符合）",
				createDataset(term), true, true, false);
		pieChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// 得到3D饼图的plot对象
		PiePlot piePlot = (PiePlot) pieChart.getPlot();
		// 设置透明度
		piePlot.setForegroundAlpha(0.5f);
		piePlot.setLabelFont((new Font("宋体", Font.PLAIN, 12)));
		piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}{1}场({2})",
				NumberFormat.getInstance(),new DecimalFormat("0.00%")));
		piePlot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}({2})",
				NumberFormat.getInstance(),new DecimalFormat("0.00%")));
		// 设置标题字体
		pieChart.getTitle().setFont(new Font("楷体", Font.BOLD, 20));
		// 设置图例类别字体
		pieChart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 14));
		frame = new ChartFrame("统计结果", pieChart);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setVisible(true);
	}
	public DefaultPieDataset createDataset(String term) {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("主队胜", this.Data_Map.get(term).Win.size());
		pieDataset.setValue("平局", this.Data_Map.get(term).Draw.size());
		pieDataset.setValue("主队负", this.Data_Map.get(term).Lose.size());
		pieDataset.setValue("未开赛", this.Data_Map.get(term).Not_Start.size());
		pieDataset.setValue("进行中", this.Data_Map.get(term).Running.size());
		return pieDataset;
	}
}
class Data
{
	public ArrayList<String> Win;
	public ArrayList<String> Draw;
	public ArrayList<String> Lose;
	public ArrayList<String> Not_Start;
	public ArrayList<String> Running;
	public Data(ArrayList<String> w, ArrayList<String> d, ArrayList<String> l, ArrayList<String> n, ArrayList<String> r)
	{
		Win = w; Draw = d; Lose = l; Not_Start = n; Running = r;
	}
}