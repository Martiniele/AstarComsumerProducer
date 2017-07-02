package astar;

import java.util.Set;

public interface IMove {
	 /** 
     * ���1����2�ĺ���·�� 
     * @param x1 ��1x���� 
     * @param y1 ��1y���� 
     * @param x2 ��2x���� 
     * @param y2 ��2y���� 
     * @param barrier ��˳���·���б� 
     * @return 
     */  
    Point move(int x1,int y1,int x2,int y2,Set<Point> barrier);
}
