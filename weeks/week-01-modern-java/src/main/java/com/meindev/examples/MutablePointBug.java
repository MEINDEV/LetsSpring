import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

public class MutablePointBug {

    static class Point {
        private int x;
        private int y;

        Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        void setX(int x){
            this.x = x;
        }

        @Override 
        public boolean equals(Object o){
            if (this == o) return true;
            if (!(o instanceof Point other)) return false;
            return x == other.x && y == other.y;
        }

        @Override 
        public int hashCode(){
            return Objects.hash(x,y);
        }
    }

    public static void main(String[] args) {
        Set<Point> visited = new HashSet<>();
        Point p = new Point(1,2);
        visited.add(p);

        System.out.println(visited.contains(p));

        p.setX(5);
        System.out.println(visited.contains(p));

        System.out.println(visited.size());
    }

}