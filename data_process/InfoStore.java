package data_process;

public class InfoStore {
	public String ID;
	public String Time;
	public String Home;
	public String Visit;
	public String Name;
	public String ZGOAL;
	public String KGOAL;
	public String State;
	public int zp;
	public int kp;
	public double WiliamD;
	public double MacaoP;
	public double CrownP;
	public double Bet365P;
	public String zzj;
	public int winG;
	public int rlw1005;
	public InfoStore(String N, String id, String time, String home, String visit, String z, String k,String state)
	{
		Name = N;
		ID = id;
		Time = time;
		Home = home;
		Visit = visit;
		ZGOAL = z;
		KGOAL = k;
		State = state;
	}
}
