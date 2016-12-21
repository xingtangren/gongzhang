package gongzhang;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * Created by dave on 2016/3/22.
 */
public class YinZhang {
   public static BufferedImage getSeal(String head,String foot,int canvasWidth,int canvasHeight){
       BufferedImage bi = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
       Graphics2D g2d = bi.createGraphics();
       //设置画笔
       g2d.setPaint(Color.WHITE);
       g2d.fillRect(0, 0, canvasWidth, canvasWidth);

       int circleRadius = Math.min(canvasWidth,canvasHeight)/2;
       /***********draw circle*************/
       g2d.setPaint(Color.red);
       g2d.setStroke(new BasicStroke(15));//设置画笔的粗度
       Shape circle = new Arc2D.Double(10,10,circleRadius*2-20,circleRadius*2-20,0,360,Arc2D.OPEN);
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);  
       g2d.draw(circle);
       
       /*****************draw 五角星******************/
       int fontSize = 30;
       Font f = new Font("宋体", Font.PLAIN, fontSize);
       FontRenderContext context = g2d.getFontRenderContext();
       Rectangle2D bounds = null;
       
       int x0=circleRadius;
       int y0= circleRadius;
       double ch = 72 * Math.PI / 180;// 圆心角的弧度数
       int x1 = x0;
       int radius = 50;
       int x2 = (int) (x0 - Math.sin(ch) * radius);
       int x3 = (int) (x0 + Math.sin(ch) * radius);
       int x4 = (int) (x0 - Math.sin(ch / 2) * radius);
       int x5 = (int) (x0 + Math.sin(ch / 2) * radius);
       int y1 = y0 - radius;
       int y2 = (int) (y0 - Math.cos(ch) * radius);
       int y3 = y2;
       int y4 = (int) (y0 + Math.cos(ch / 2) * radius);
       int y5 = y4;

       int bx = (int) (x0 + Math.cos(ch) * Math.tan(ch / 2) * radius);
       int by = y2;

       Polygon a = new Polygon();
       Polygon b = new Polygon();

       a.addPoint(x2, y2);
       a.addPoint(x5, y5);
       a.addPoint(bx, by);
       b.addPoint(x1, y1);
       b.addPoint(bx, by);
       b.addPoint(x3, y3);
       b.addPoint(x4, y4);

       g2d.drawPolygon(a);
       g2d.drawPolygon(b);
       g2d.fillPolygon(a);
       g2d.fillPolygon(b);


       /*****************draw foot*******************/
       fontSize = 36;
       f = new Font("仿宋",Font.PLAIN ,fontSize);
       context = g2d.getFontRenderContext();
       bounds = f.getStringBounds(foot,context);
       g2d.setFont(f);
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
               RenderingHints.VALUE_ANTIALIAS_ON);  
       String tempStr=new String();  
       int orgStringWight=g2d.getFontMetrics().stringWidth(foot);  
       int orgStringLength=foot.length();  
       double tempx=circleRadius - bounds.getCenterX()*0.9;  
       double tempy=circleRadius*1.5 - bounds.getCenterY();  
       while(foot.length()>0)  
       {  
           tempStr=foot.substring(0, 1);  
           foot=foot.substring(1, foot.length());  
           g2d.drawString(tempStr, (float)tempx, (float)tempy);  
           tempx=(double)(tempx+(double)orgStringWight/(double)orgStringLength*0.9);
       }  
       
       /***************draw string head**************/
       fontSize = 50;
       f = new Font("方正粗宋简体",Font.PLAIN ,fontSize);
       context = g2d.getFontRenderContext();
       bounds = f.getStringBounds(head,context);

       double msgWidth = bounds.getWidth();
       int countOfMsg = head.length();
       double interval = msgWidth/(countOfMsg-3);//计算间距


       double newRadius = circleRadius + bounds.getY()-30;//bounds.getY()是负数，这样可以将弧形文字固定在圆内了。-5目的是离圆环稍远一点
       double radianPerInterval = 2 * Math.asin(interval / (2 * newRadius));//每个间距对应的角度

       //第一个元素的角度
       double firstAngle;
       if(countOfMsg % 2 == 1){//奇数
           firstAngle = (countOfMsg-1)*radianPerInterval/2.0 + Math.PI/2+0.08;
       }else{//偶数
           firstAngle = (countOfMsg/2.0-1)*radianPerInterval + radianPerInterval/2.0 +Math.PI/2+0.08;
       }

       for(int i = 0;i<countOfMsg;i++){
           double aa = firstAngle - i*radianPerInterval;
           double ax = newRadius * Math.sin(Math.PI/2 - aa);//小小的trick，将【0，pi】区间变换到[pi/2,-pi/2]区间
           double ay = newRadius * Math.cos(aa-Math.PI/2);//同上类似，这样处理就不必再考虑正负的问题了
           AffineTransform transform = AffineTransform .getRotateInstance(Math.PI/2 - aa);// ,x0 + ax, y0 + ay);
           Font f2 = f.deriveFont(transform);
           g2d.setFont(f2);	
           g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                   RenderingHints.VALUE_ANTIALIAS_ON);  
           g2d.drawString(head.substring(i,i+1), (float) (circleRadius+ax),  (float) (circleRadius - ay));
       }

       g2d.dispose();//销毁资源
       return bi;
   }
   public static int percent = 0;
   public static void generateSealByExcel(String savePath,String excelPath,final JProgressBar progressBar,final JLabel progressLabel){
	   int canvasWidth = 475;
       int canvasHeight = 475;
       File excelFile = new File(excelPath);
       List cols = new ArrayList<String>();
       cols.add("name");
       savePath = savePath.endsWith("\\")?savePath:savePath+"\\";
       percent = 0;
       try {
    	   List<Map> resultList = PoiFileUtil.readExcel(excelFile, 0, cols);
    	   double finished=0.0;
    	   double total = Double.parseDouble(resultList.size()+"");
    	   
    	   String per = "";
//    	   new Thread(new Runnable() {//开辟一个工作线程
//   			@Override
//   			public void run() {
//   				try {
//                        while(percent<=100){
//             			   progressBar.setValue(percent);
//             			   progressLabel.setText(percent+"%"); 
//                        }
//   				} catch (Exception e1) {
//   					e1.printStackTrace();
//   				}
//   			}
//   		   }).start();
    	   for(Map map : resultList){
    		   BufferedImage image = YinZhang.getSeal(map.get("name")+"", "计划生育专用章", canvasWidth, canvasHeight);
    		   try{
    			   ImageIO.write(image, "PNG", new File(savePath+map.get("name")+".png"));
    			   finished = finished+1;
    			   per = Math.floor(finished/total*100)+"";
    			   if(per.indexOf(".")!=-1){
    				   per = per.substring(0, per.lastIndexOf("."));
    			   }
    			   percent = Integer.parseInt(per);
    		   }
    		   catch (IOException e){
    			   e.printStackTrace();
    		   }
    	   }
    	   percent++;
       } catch (IOException e) {
    	   e.printStackTrace();
       }
   }
   
   public static void generateSealByInput(String savePath,String[] sealArr,JProgressBar progressBar,JLabel progressLabel){
	   int canvasWidth = 475;
       int canvasHeight = 475;
       try {
    	   savePath = savePath.endsWith("\\")?savePath:savePath+"\\";
    	   for(String name : sealArr){
    		   BufferedImage image = YinZhang.getSeal(name, "计划生育专用章", canvasWidth, canvasHeight);
    		   try{
    			   ImageIO.write(image, "PNG", new File(savePath+name+".png"));
    		   }
    		   catch (IOException e){
    			   e.printStackTrace();
    		   }
    	   }
       } catch (Exception e) {
    	   e.printStackTrace();
       }
   }
   
   public static void main(String[] args){
//	   new Thread(new Runnable() {//开辟一个工作线程
//			@Override
//			public void run() {
//				try {
//					progressLabel.setText("1.检查数据合法性...");
//					Thread.sleep(3000);//模仿检测数据合法性
//					progressLabel.setText("2.正在导入数据...");
//					Thread.sleep(4000);//模仿导入数据
//					progressLabel.setText("3.导入成功!");
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}).start();
   }
}