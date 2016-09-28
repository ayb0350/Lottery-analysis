package data_process;

import java.io.IOException;

import display.Lists;
import display.Statistics;

public class DoSomething implements Runnable { 
	public Statistics s;
	public DoSomething() throws IOException
	{
		s = new Statistics();
	}
    public void run() { 
    	try {
    		this.s.textarea.append("数据更新中...\n");
			Lists.sta_over = false;
			this.s.UpData();
			if(!Lists.sta_over)
			{
				Lists.datatext.setText("数据更新完成!");
				this.s.textarea.append("数据更新完成!\n");
			}
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}