package astar;

public class Point {
	int x;  
    int y;  
    int gCost;  
    int hEstimate;  
    int fTotal;  
    Point prev;  
    int level=1;  
      
    public String getKey(){  
        return x+"_"+y;  
    }  
    public Point(int x, int y) {  
        super();  
        this.x = x;  
        this.y = y;  
    }  
  
      
    public Point(int x, int y, int gCost) {  
        super();  
        this.x = x;  
        this.y = y;  
        this.gCost = gCost;  
    }  
  
  
    @Override  
    public int hashCode() {  
        final int prime = 31;  
        int result = 1;  
        result = prime * result + x;  
        result = prime * result + y;  
        return result;  
    }  
  
    @Override  
    public boolean equals(Object obj) {  
        if (this == obj)  
            return true;  
        if (obj == null)  
            return false;  
        if (getClass() != obj.getClass())  
            return false;  
        Point other = (Point) obj;  
        if (x != other.x)  
            return false;  
        if (y != other.y)  
            return false;  
        return true;  
    }  
}
