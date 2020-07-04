package cn.edu.zucc.personplan.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class FrmMain extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JMenuBar menubar=new JMenuBar(); ;
    private JMenu menu_plan=new JMenu("�ƻ�����");
    private JMenu menu_step=new JMenu("�������");
    private JMenu menu_static=new JMenu("��ѯͳ��");
    private JMenu menu_more=new JMenu("����");

    private JMenuItem  menuItem_AddPlan=new JMenuItem("�½��ƻ�");
    private JMenuItem  menuItem_DeletePlan=new JMenuItem("ɾ���ƻ�");
    private JMenuItem  menuItem_AddStep=new JMenuItem("��Ӳ���");
    private JMenuItem  menuItem_DeleteStep=new JMenuItem("ɾ������");
    private JMenuItem  menuItem_startStep=new JMenuItem("��ʼ����");
    private JMenuItem  menuItem_finishStep=new JMenuItem("��������");
    private JMenuItem  menuItem_moveUpStep=new JMenuItem("��������");
    private JMenuItem  menuItem_moveDownStep=new JMenuItem("��������");

    private JMenuItem  menuItem_modifyPwd=new JMenuItem("�����޸�");

    private JMenuItem  menuItem_static1=new JMenuItem("ͳ��1");


	private FrmLogin dlgLogin=null;
	private JPanel statusBar = new JPanel();


	public FrmMain(){
		
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setTitle("����ô����ƽ̨");
		dlgLogin=new FrmLogin(this,"����ô������¼����",true);
		dlgLogin.setVisible(true);
	    //�˵�


	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
