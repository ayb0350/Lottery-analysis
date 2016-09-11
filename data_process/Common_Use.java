package data_process;

import java.util.ArrayList;

public class Common_Use {
	
	public static boolean Satisfy(ArrayList<InfoStore> Q, String flag)
	{
		if(flag == "3Z1" | flag == "3Z2" | flag == "3K1")
		{
			if(Q.size() == 5)
			{
				if(!Q.get(0).Name.equals(Q.get(1).Name) & Q.get(1).Name.equals(Q.get(2).Name) &
					Q.get(2).Name.equals(Q.get(3).Name) & !Q.get(3).Name.equals(Q.get(4).Name))
				{
					if(flag == "3Z1")
					{
						for(int i = 1; i < 4; i++)
							if(Q.get(i).WiliamD < 3.30 | Q.get(i).MacaoP != -0.5)
								return false;
						return true;
					}
					else if(flag == "3Z2")
					{
						for(int i = 1; i < 4; i++)
							if(Math.abs(Q.get(i).zp - Q.get(i).kp) > 5 | Q.get(i).CrownP != -0.25)
								return false;
						return true;
					}
					else if(flag == "3K1")
					{
						boolean temp = false;
						for(int i = 1; i < 4; i++)
							if(Q.get(i).MacaoP == -1)
								temp = temp | true;
						for(int i = 1; i < 4; i++)
							if(Q.get(i).WiliamD < 3.60)
								temp = false;
						return temp;
					}
				}
			}
		}
		else if(flag == "2P1" | flag == "2Z1" | flag == "2P2")
		{
			if(Q.size() == 5)
			{
				if(!Q.get(1).Name.equals(Q.get(2).Name) & Q.get(2).Name.equals(Q.get(3).Name) & !Q.get(3).Name.equals(Q.get(4).Name))
				{
					if(flag == "2P1")
					{
						boolean temp = false;
						if((Q.get(2).Bet365P == -0.5 & Q.get(3).Bet365P == -0.25) | (Q.get(3).Bet365P == -0.5 & Q.get(2).Bet365P == -0.25))
							temp = true;
						else
							return false;
						if(Q.get(2).WiliamD <= 3.25 & Q.get(3).WiliamD <= 3.25)
							temp = true;
						else
							return false;
						return temp;
					}
					else if(flag == "2Z1")
					{
						//for(int i = 2; i < 4; i++)
							//System.out.println(Q.get(i).zp + "  " + Q.get(i).kp + "  " + Q.get(i).zzj + "  " + Q.get(i).Bet365P + "  " + Q.get(i).winG);
						for(int i = 2; i < 4; i++)
							if(Q.get(i).zp >= Q.get(i).kp | (!Q.get(i).zzj.equals("平") & !Q.get(i).zzj.equals("负")) | Q.get(i).Bet365P != -0.25 | Q.get(i).winG <= 6)
								return false;
						return true;
					}
					else if(flag == "2P2")
					{
						for(int i = 2; i < 4; i++)
							if(Q.get(i).winG <= 5)
								return false;
						if((Q.get(2).Bet365P == 0.5 & Q.get(3).Bet365P == -0.25) | (Q.get(3).Bet365P == 0.5 & Q.get(2).Bet365P == -0.25))
							return true;
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean is_num(String str)
	{
		if(str.equals("") | str.equals("-"))
			return false;
		if(str.indexOf("/")!=-1)
		{
			String str1 = str.substring(0,str.indexOf("/"));
			String str2 = str.substring(str.indexOf("/")+1,str.length());
			if(str1.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")&&
					str2.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"))
				return true;
			else
				return false;
		}
		else
			return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	//文字转换
	public static double transform(String str)
	{
		if(str.equals("受三球"))
			return 3;
		if(str.equals("受两球半/三球"))
			return 2.75;
		if(str.equals("受两球半"))
			return 2.5;
		if(str.equals("受两球/两球半"))
			return 2.25;
		if(str.equals("受两球"))
			return 2;
		if(str.equals("受球半/两球"))
			return 1.75;
		if(str.equals("受球半"))
			return 1.5;
		if(str.equals("受一球/球半"))
			return 1.25;
		if(str.equals("受一球"))
			return 1;
		if(str.equals("受半球/一球"))
			return 0.75;
		if(str.equals("受半球"))
			return 0.5;
		if(str.equals("受平手/半球"))
			return 0.25;
		if(str.equals("平手"))
			return 0;
		if(str.equals("平手/半球"))
			return -0.25;
		if(str.equals("半球"))
			return -0.5;
		if(str.equals("半球/一球"))
			return -0.75;
		if(str.equals("一球"))
			return -1;
		if(str.equals("一球/球半"))
			return -1.25;
		if(str.equals("球半"))
			return -1.5;
		if(str.equals("球半/两球"))
			return -1.75;
		if(str.equals("两球"))
			return -2;
		if(str.equals("两球/两球半"))
			return -2.25;
		if(str.equals("两球半"))
			return -2.5;
		if(str.equals("两球半/三球"))
			return -2.75;
		if(str.equals("三球"))
			return -3;
		return 10;
	}
	public static double parsedata(String str, double default_data)
	{
		if(is_num(str))
			return Double.parseDouble(str);
		else
			return default_data;
	}
}