package cn.edu.zucc.personplan.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import cn.edu.zucc.personplan.PersonPlanUtil;
import cn.edu.zucc.personplan.model.BeanUser;
import cn.edu.zucc.personplan.util.BaseException;

public class FrmRegister extends JDialog implements ActionListener {
	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private Button btnOk = new Button("ע��");
	private Button btnCancel = new Button("ȡ��");
	private JLabel labelUser = new JLabel("�û���");
	private JLabel labelPwd = new JLabel("���룺");
	private JLabel labelPwd2 = new JLabel("���룺");
	private JLabel labelSex = new JLabel("�Ա�:  ");
	private JLabel labelSpace = new JLabel("������ѡ�");
	private JLabel labelTelNumber = new JLabel("�ֻ���");
	private JLabel labelEmail = new JLabel("���䣺");
	private JLabel labelCity = new JLabel("���У�");
	private JTextField edtCity = new JTextField(20);
	private JTextField edtEmail= new JTextField(20);
	private JTextField edtUserName = new JTextField(20);
	private JTextField edtTelNumber = new JTextField(20);
	private JPasswordField edtPwd = new JPasswordField(20);
	private JPasswordField edtPwd2 = new JPasswordField(20);
	private JRadioButton man = new JRadioButton("��");
	private JRadioButton woman = new JRadioButton("Ů");
	private JRadioButton secret = new JRadioButton("����");
	private ButtonGroup sex = new ButtonGroup();
	public FrmRegister(Dialog f, String s, boolean b) {
		super(f, s, b);
		sex.add(woman);
		sex.add(man);
		sex.add(secret);
		secret.setSelected(true);
		toolBar.add(this.btnOk);
		btnOk.setBackground(Color.cyan);
		btnCancel.setBackground(Color.cyan);
		toolBar.add(btnCancel);
		toolBar.setLayout(new GridLayout(1,2,5,0));
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);
		workPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		workPane.add(labelUser);
		workPane.add(edtUserName);
		workPane.add(labelPwd);
		workPane.add(edtPwd);
		workPane.add(labelPwd2);
		workPane.add(edtPwd2);
		workPane.add(labelTelNumber);
		workPane.add(edtTelNumber);
		workPane.add(labelSex);
		workPane.add(man);
		workPane.add(woman);
		workPane.add(secret);
		workPane.add(labelSpace);
		workPane.add(labelEmail);
		workPane.add(edtEmail);
		workPane.add(labelCity);
		workPane.add(edtCity);
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.setSize(300, 260);
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);
		this.validate();
		this.setResizable(false);
		this.btnCancel.addActionListener(this);
		this.btnOk.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnCancel)
			this.setVisible(false);
		else if(e.getSource()==this.btnOk){
			String userName=this.edtUserName.getText();
			String userPassword=new String(this.edtPwd.getPassword());
			String userPassword2=new String(this.edtPwd2.getPassword());
			String userSex=this.secret.getText();
			String userTelNumber=this.edtTelNumber.getText();
			String userEmail = this.edtEmail.getText();
			String userCity = this.edtCity.getText();
			if(this.man.isSelected()){
				userSex=this.man.getText();
			}else if(this.woman.isSelected()){
				userSex=this.woman.getText();
			}
			try {
				BeanUser user=PersonPlanUtil.userManager.reg(userName, userSex, userPassword, userPassword2, userTelNumber,
						userEmail,userCity);
				JOptionPane.showMessageDialog(null, "ע��ɹ�", "�ɹ�",JOptionPane.ERROR_MESSAGE);
				this.setVisible(false);
			} catch (BaseException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(),"����",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
		}
			
		
	}


}
