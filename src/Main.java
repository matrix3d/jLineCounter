/**
 * Created with IntelliJ IDEA.
 * User: lizhi
 * Date: 13-7-9
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
import javax.swing.*;
public class Main {
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e){

		}
		View view = new View();
		view.setSize(400,400);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setVisible(true);
	}
}
