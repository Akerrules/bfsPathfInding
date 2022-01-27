import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javax.swing.JFrame;
import java.awt.event.MouseMotionListener;  


public class main implements MouseListener, KeyListener, MouseMotionListener{ 
    public class points{
        int distance;
        int x, y;
        boolean isvisited;
        points(int x, int y){
            this.x = x;
            this.y = y;
            distance=0;
            isvisited = false;
        }
        public boolean getIsVisited(){return isvisited;}
        public int getDistance(){return distance;}
        public void setIsVisited(boolean bool){isvisited=bool;}
        public void setDistance(int distance){this.distance=distance;}
    }
    boolean[][] visitedMatrix = new boolean[50][50];
    boolean[][] obstacleBlock = new boolean[50][50];
    ArrayList<points> obstacleBlockList = new ArrayList<>();
    ArrayList<points> visitedBlocks = new ArrayList<>();
    Stack<points> bfsPath = new Stack<>();

    points[][] prev = new points[50][50];
    int width  = 500;
    int height = 500;
    JFrame frame;
    myCanvas newCanvas;
    points start, end;
    boolean isStart, isEnd;
    public main(){
        start = new points(0,0);
        end = new points(10,10);
        frame = new JFrame("Breadth First Search");  
        newCanvas = new myCanvas();
        frame.setSize(width, height);  
        frame.add(newCanvas);
        newCanvas.addMouseListener(this);
        newCanvas.addMouseMotionListener(this);
        newCanvas.addKeyListener(this);
        //frame.setResizable(false);
        frame.setLocationRelativeTo(null);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setVisible(true);  
    }
    private class myCanvas extends Canvas{
    
        @Override
        public void paint(Graphics g){
            g.setColor(Color.BLACK);
            for (int i = 0; i <=50; i++) {
                g.drawLine(0, i*10, width, i*10);
                g.drawLine(i*10, 0, i*10, height);
            }
            g.setColor(Color.blue);
            g.fillRect(start.x, start.y, 10, 10);
            g.setColor(Color.red);
            g.fillRect(end.x, end.y, 10, 10);

            g.setColor(Color.BLACK);
            //draw obstacle
            for (int i = 0; i < obstacleBlockList.size(); i++) {
                g.fillRect(obstacleBlockList.get(i).x, obstacleBlockList.get(i).y, 10, 10);
            }
            //bfs draw
            g.setColor(Color.orange);
            for (int i = 0; i < visitedBlocks.size(); i++) {
                for (int j = 0; j < Math.pow(10, 5); j++) {  
                }
                g.fillRect( visitedBlocks.get(i).x,  visitedBlocks.get(i).y, 10, 10);
                
            }
         
            g.setColor(Color.cyan);
           while(!bfsPath.empty()){
                for (int j = 0; j < Math.pow(10, 6); j++) {  
                }
                points tmpPoint = bfsPath.pop();
                g.fillRect(tmpPoint.x,  tmpPoint.y, 10, 10);
            }
           
        }    
    }
    public void BFS(points point){
        Queue<points> queue = new LinkedList<>();
        queue.add(point);
        visitedMatrix[point.x/10][point.y/10] = true;
        while(!queue.isEmpty()){
            int x = queue.peek().x;
            int y  = queue.peek().y; 
          //  System.err.println("currently at: " + x+ " Y:" + y);
          //  int distance = queue.peek().distance;
           queue.remove();
            //right
            if(validate(x+10, y, end, visitedMatrix)){
                queue.add(new points(x+10,y));
                visitedMatrix[(x+10)/10][y/10] = true;
                visitedBlocks.add(new points(x+10,y));
                prev[(x+10)/10][y/10]  = new points(x,y);
                newCanvas.repaint();

           //     System.err.println("looked at right: " + (x+10)+ " Y:" + y);
            }else if(end.x==x+10 && end.y==y){
                prev[(x+10)/10][y/10]  = new points(x,y);
                break;
            }
            //left
            if(validate(x-10, y, end, visitedMatrix)){
                queue.add(new points(x-10,y));
                visitedMatrix[(x-10)/10][y/10] = true;
                visitedBlocks.add(new points(x-10,y));
                prev[(x-10)/10][y/10]  = new points(x,y);
                newCanvas.repaint();

           //     System.err.println("looked at left: " + (x-10)+ " Y:" + y);

            }else if(end.x==x-10 && end.y==y){
                prev[(x-10)/10][y/10]  = new points(x,y);

                break;
            }
            //down
            if(validate(x, y+10, end, visitedMatrix)){
                queue.add(new points(x,y+10));
                visitedMatrix[x/10][(y+10)/10] = true;
                visitedBlocks.add(new points(x,y+10));
                prev[x/10][(y+10)/10]  = new points(x,y);
                newCanvas.repaint();

            //    System.err.println("looked at down: " + x+ " Y:" +(y+10));
            }else if(end.x==x && end.y==y+10){
                prev[x/10][(y+10)/10]  = new points(x,y);
                break;
            }
//up
            if(validate(x, y-10, end, visitedMatrix)){
                queue.add(new points(x,y-10));
             //   System.err.println("looked at up: " + x+ " Y:" + (y-10));
                visitedMatrix[x/10][(y-10)/10] = true;
                visitedBlocks.add(new points(x,y-10));
                prev[x/10][(y-10)/10]  = new points(x,y);

                newCanvas.repaint();
            }else if(end.x==x && end.y==y-10){
                prev[x/10][(y-10)/10]  = new points(x,y);
                break;
            }

        }
        constructPath();
    }
    public boolean validate(int x, int y, points end, boolean[][] isvisited){
       // System.err.println((end.x!=x && end.y!=y) + "x:"+x+" y:"+y + " endx:"+end.x+" endy:"+end.y);
        return  y>=0 && x>=0 && y<500 && x<500 && !(end.x==x && end.y==y)  && !isvisited[x/10][y/10] && !obstacleBlock[x/10][y/10];
    }

    public void constructPath(){
       points tmp = end;
        while(!(tmp.x==start.x && tmp.y==start.y)){
            if(prev[tmp.x/10][tmp.y/10]!=null){
                bfsPath.push(prev[tmp.x/10][tmp.y/10]);
            }
            tmp = prev[tmp.x/10][tmp.y/10];
        }
        
        newCanvas.repaint();
    }
    public static void main(String s[]) {  
        main app = new main();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        // System.out.println("X:" + e.getX() + " Y:"+ e.getY());
        System.err.println("X:" + (e.getX() -  (e.getX()%10))   + " Y:" + (e.getY() -  (e.getY()%10)));
        if(isStart){
            visitedMatrix[start.x/10][start.y/10] = false;
            start.x = (e.getX() -  (e.getX()%10));
            start.y = (e.getY() -  (e.getY()%10));
            visitedMatrix[start.x/10][start.y/10] = true;
        }else if(isEnd){
            end.x = (e.getX() -  (e.getX()%10));
            end.y = (e.getY() -  (e.getY()%10));
        }else{
            int tmpx= (e.getX() -  (e.getX()%10));
            int tmpy = (e.getY() -  (e.getY()%10));
            if(!obstacleBlock[tmpx/10][tmpy/10]){
                obstacleBlock[tmpx/10][tmpy/10] = true;
                obstacleBlockList.add(new points(tmpx, tmpy));
            }else{
                obstacleBlock[(e.getX() -  (e.getX()%10))/10][(e.getY() -  (e.getY()%10))/10] = false;
                for (int i = 0; i < obstacleBlockList.size(); i++) {
                   if(obstacleBlockList.get(i).x == tmpx && obstacleBlockList.get(i).y == tmpy ){
                        obstacleBlockList.remove(i);
                        break;
                   }
                }
            }
        }
        newCanvas.repaint();

    }
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        System.err.println(e.getKeyChar());
        if(e.getKeyChar()=='c'){
            clear();
            System.err.println("clear");
        }
        
    }
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode() == KeyEvent.VK_S){
            isStart = true;
            visitedBlocks.clear();
            prev = new points[50][50];
            bfsPath.clear();
            visitedMatrix = new boolean[50][50];
            newCanvas.repaint();

        }else if ( e.getKeyCode() == KeyEvent.VK_E){
            isEnd = true;
            visitedBlocks.clear();
        prev = new points[50][50];
        bfsPath.clear();
        visitedMatrix = new boolean[50][50];
        newCanvas.repaint();

        }
        if(e.getKeyCode() == KeyEvent.VK_B){
            BFS(start);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        isEnd = false;
        isStart = false;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
            int tmpx= (e.getX() -  (e.getX()%10));
            int tmpy = (e.getY() -  (e.getY()%10));
           
                obstacleBlock[tmpx/10][tmpy/10] = true;
                obstacleBlockList.add(new points(tmpx, tmpy));
            
            newCanvas.repaint();
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    public void clear(){
        obstacleBlock = new boolean[50][50];
        obstacleBlockList.clear();
        visitedBlocks.clear();
        prev = new points[50][50];
        bfsPath.clear();
        visitedMatrix = new boolean[50][50];
        newCanvas.repaint();
    }


    }  