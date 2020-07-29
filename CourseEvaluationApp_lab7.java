import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
//required for border
import javax.swing.BorderFactory;
import javax.swing.border.Border;
//required for file IO
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
//required for exception
import java.io.IOException;

//Header panel
class HeaderPanel extends JPanel{
   private JLabel header;
   public HeaderPanel(){
      	header = new JLabel("Course Evaluation Form");
      	add(header);
   }
}

//Form Panel
class FormPanel extends JPanel implements ActionListener, ItemListener{
   //list all UI components for the panel
   JLabel l_name;
   JTextField tf_name;
   JLabel l_matric;
   JTextField tf_matric;
   JLabel l_code;
   JComboBox<String> cb_code;
   JLabel l_rating;
   JLabel l_outcome;
   JButton b_submit;
   JButton b_clear;
   JCheckBox cb1;
   JCheckBox cb2;
   JLabel l_output; 
   JScrollPane jsp;
   Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
   
   //global variable  
   String output="";
   String code_selection="";
   String rb_selection="";
   String cb_selection="";
   String filePath="evaluationform1.txt"; //in the same directory
      
   public FormPanel(){   
      setLayout(new FlowLayout(FlowLayout.LEFT));     
      
      l_name = new JLabel("Name");
      l_name.setPreferredSize(new Dimension(150, 20));
      l_name.setBorder(border);
      
      add(l_name);
      tf_name = new JTextField(26);
      add(tf_name);
      
      //input name will not accept any number.
      tf_name.addKeyListener(new KeyAdapter() {
         public void keyTyped(KeyEvent e) {
         char c=e.getKeyChar(); 
            if(Character.isAlphabetic(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE) || (c==KeyEvent.VK_SPACE)) {
                e = e;
            }
           else{
                e.consume();
            }
        }
});
     
      l_matric = new JLabel("Matric No.");
      l_matric.setPreferredSize(new Dimension(150, 20));
      l_matric.setBorder(border);
      add(l_matric);
      tf_matric = new JTextField(26);
      add(tf_matric);   
      
      String[] courses={"[Select]", "KK14203 Oriented Object Programming", "KT14203 Computer Organization and Architecture", "KT14403 Discrete Structure"};
      
      l_code = new JLabel("Course Code");
      l_code.setPreferredSize(new Dimension(150, 20));
      add(l_code);
      cb_code = new JComboBox<String>(courses);
      add(cb_code); 
      
      //JComboBox action listener
      cb_code.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae){
            //get selected item
            code_selection = (String) cb_code.getSelectedItem();
         }
      });  
      
      l_rating = new JLabel("Rating");
      l_rating.setPreferredSize(new Dimension(150, 20));
      add(l_rating);
      
      //Radio buttons and action listener
      JRadioButton rb1 = new JRadioButton("1");
      rb1.addActionListener(this);
      add(rb1);
      JRadioButton rb2 = new JRadioButton("2");
      rb2.addActionListener(this);
      add(rb2);
      JRadioButton rb3 = new JRadioButton("3");
      rb3.addActionListener(this);
      add(rb3);
      JRadioButton rb4 = new JRadioButton("4");
      rb4.addActionListener(this);
      add(rb4);
      JRadioButton rb5 = new JRadioButton("5");
      rb5.addActionListener(this);
      add(rb5);
      
      //define button group
      ButtonGroup bg = new ButtonGroup();
      bg.add(rb1);
      bg.add(rb2);
      bg.add(rb3);
      bg.add(rb4);
      bg.add(rb5);
      
      l_outcome = new JLabel("Outcome");
      l_outcome.setPreferredSize(new Dimension(150, 20));
      add(l_outcome);
      
      //checkbox and  item listener
      cb1 = new JCheckBox("Basic knowledge");
      cb1.addItemListener(this);
      add(cb1);
      cb2 = new JCheckBox("Advanced knowledge");
      cb2.addItemListener(this);
      add(cb2);
      
      b_submit = new JButton("Submit");
      add(b_submit);
      b_clear = new JButton("Clear");
      add(b_clear);

      //handle button submit action listener
      //view the input to output label
      //and write to file
      b_submit.addActionListener(new ActionListener(){           
         public void actionPerformed(ActionEvent e){  
            //call method
            if(printOutput()){
               writeInput();  
               
               //show dialog message if input is succesfully saved
               JOptionPane.showMessageDialog(null, "Data saved successfully");
            }   
         }  
      });

      //handle button clear action listener
      b_clear.addActionListener(new ActionListener(){  
         public void actionPerformed(ActionEvent e){  
            l_output.setText("Output");  
            tf_name.setText("");
            tf_matric.setText(""); 
            cb_code.setSelectedIndex(0);
            bg.clearSelection();
            cb1.setSelected(false);
            cb2.setSelected(false);
         }  
      });
      
      l_output = new JLabel("Output");
      l_output.setBorder(border);
      l_output.setVerticalAlignment(JLabel.TOP);
      
      //add output label to scrollpane
      jsp = new JScrollPane(l_output);
      jsp.setPreferredSize(new Dimension(450,120));
      add(jsp);     
   }

   //handle radio button selection
   public void actionPerformed(ActionEvent ae) {
      rb_selection = ae.getActionCommand();    	   
   }
   
   //handle item listener for checkbox
   public void itemStateChanged(ItemEvent ie) {
      JCheckBox check = (JCheckBox)ie.getSource();
      cb_selection += check.getText() + " ";   
   }
   
   //method to print output to lbl_output
   public boolean printOutput(){
      output = "<html>";
      output += "Thank you for your Evaluation<br><br>"; 
      output += "Name: " + tf_name.getText() + "<br>";
      output += "Matric: " + tf_matric.getText() + "<br>";
      
      if(code_selection.equals("[Select]") || cb_code.getSelectedItem().equals("")  || tf_name.getText().equals("") || tf_matric.getText().equals("") || rb_selection.equals("") || cb_selection.equals("")){
         JOptionPane.showMessageDialog(null, "Please fill the Evaluation.");
         return false;
      }
      
      output += "Course: " + code_selection + "<br>";
      output += "Rating: " + rb_selection + "<br>";
      output += "Outcome: " + cb_selection + "<br>";
      output += "</html>";          
      l_output.setText(output);
      jsp.getViewport().revalidate();
      return true;
    }

    //write to file
    public void writeInput(){
      File file = new File(filePath);
		FileWriter fr = null;
		BufferedWriter br = null;
		PrintWriter pr = null;
      
      String input = " " + tf_name.getText() + " , " + tf_matric.getText() + " , " + code_selection + " , " + rb_selection + " , " + cb_selection;
      
      //exception implementation
		try {
			// to append to file, you need to initialize FileWriter using below constructor
			fr = new FileWriter(file, true);
			br = new BufferedWriter(fr);
			pr = new PrintWriter(br);
			pr.println(input);
		} catch (IOException e) {			
         l_output.setText(e.toString());
		} finally {
			try {
				pr.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				l_output.setText(e.toString());
			}
		}
    }
}

class MenuActionListener implements ActionListener {
   FormPanel fp;
   //receive FormPanel class to this constructor
   public MenuActionListener(FormPanel p){
      fp = p;
}
    
   public void actionPerformed(ActionEvent e) {      
      BufferedReader reader;
	   try {
			reader = new BufferedReader(new FileReader(fp.filePath));
			String line = reader.readLine();
         String output="<html>";
			while (line != null) {
				output += line + "<br>";
				// read next line
				line = reader.readLine();
			}
         output += "<br>";
         fp.l_output.setText(output);
			reader.close();
		} catch (IOException io) {
			fp.l_output.setText(io.toString());
		}
  }
}

class MenuActionListener2 implements ActionListener {
   FormPanel fp;
   //receive FormPanel class to this constructor
   public MenuActionListener2(FormPanel p){
      fp = p;
}
    
   public void actionPerformed(ActionEvent e) {   
      
      //show confirm dialog to exit application
      int response = JOptionPane.showConfirmDialog(null,"Do you want to Exit? ", 
     "Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

     if (response == JOptionPane.YES_OPTION)
     {
        System.exit(0);
     } 
  }
}

//run the application using this main
public class CourseEvaluationApp_lab7 {  
   public static void main(String[] 	args) {  
      JFrame f = new JFrame("Course Evaluation");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
      //load panels
      HeaderPanel h = new 	HeaderPanel();
      FormPanel fp = new FormPanel();
      
      JMenuBar mb = new JMenuBar(); 
      // create a menu 
      JMenu x = new JMenu("Menu"); 
      
      // create menuitems 
      JMenuItem m1 = new JMenuItem("Load Data"); 
      // attach listener and send FormPanel class
      m1.addActionListener(new MenuActionListener(fp));
      
      JMenuItem m2 = new JMenuItem("Exit");
      m2.addActionListener(new MenuActionListener2(fp));
 
      // add menu items to menu 
      x.add(m1); 
      x.add(m2);
     
      // add menu to menu bar 
      mb.add(x); 
      // add menubar to frame 
      f.setJMenuBar(mb);  
               
      //add panels to frame       
      f.add(h,BorderLayout.NORTH);
      f.add(fp, BorderLayout.CENTER);
      f.setSize(490,420);
      f.setVisible(true);
   }  
}

  