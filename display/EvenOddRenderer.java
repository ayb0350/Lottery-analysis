package display;


import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import data_process.Analysis.CompPair;
import display.Lists;

class EvenOddRenderer extends JTextArea implements TableCellRenderer
{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean is_Red(String str, int col)
	{
		if(str.equals("") | str.equals("N/A"))
			return false;
		for(CompPair<String,String,Double> P : Lists.ArrayRed){
			if(Lists.tableModel.getColumnName(col).equals(P.first)){
				if(P.third == -1.0 & !P.second.equals(">") & !P.second.equals("=") & !P.second.equals("<")){
					if(P.second.indexOf("o") != -1){
						String s[] = P.second.split("o");
						for(String st : s)
						{
							if(str.equals(st))
								return true;
						}
						return false;
					}
					return P.second.equals(str);
				}
				else{
					String st = str.indexOf("个")!=-1|str.indexOf("场")!=-1|str.indexOf("球")!=-1|str.indexOf("%")!=-1?
							str.substring(0, str.length()-1):str;
					double d;
					try {
						d = Double.parseDouble(st);
					}catch(NumberFormatException e){
						return false;
					}
					if(P.second.indexOf("&") != -1)
						return d > P.third & d < P.forth;
					switch(P.second){
					case "<": return d < P.third;
					case "=": return d == P.third;
					case ">": return d > P.third;
					default: return false;
					}
				}
			}
		}
		return false;
	}
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, 
			boolean hasFocus, int row, int column) 
	{
		Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		DEFAULT_RENDERER.setHorizontalAlignment(JLabel.CENTER);
		Color background;
		if(value == null)
			System.out.println(table.getColumnName(column));
		String str = value.toString();
		if (column > 0)
		{
			if(is_Red(str,column))
				background = Color.RED;
			else
				background = Color.WHITE;
		}
		else
			background = Color.WHITE;
		if(isSelected)
			renderer.setBackground(background == Color.WHITE ? table.getSelectionBackground():background);
		else
			renderer.setBackground(background);
		return renderer;
	}
}