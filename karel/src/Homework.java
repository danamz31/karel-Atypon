import stanford.karel.*;


public class Homework extends SuperKarel {
    private int length = 1;
    private int height = 1;
    private int innerSquareLength;
    private int innerSquareHeight;
    private int count = 0;
    private void count(){
        count++;
        System.out.println(count);
    }
    public void move(){
        super.move();
        count();
    }
    public void putBeeper(){
        if(noBeepersPresent())
            super.putBeeper();
    }
    private void setLength(){
        while (frontIsClear()) {
            move();
            length++;
        }
        turnLeft();
    }
    private void setHeight(){
        while (frontIsClear()){
            move();
            height++;
        }
        turnLeft();
    }
    private void setInnerSquareDimensions(){
        if(length<=height){
            innerSquareLength = length - 2;
            if(length%2==0){
                if(height%2==0){
                    //dimensions are both even or equal
                    innerSquareHeight = innerSquareLength;
                }else{
                    //the smallest is even and the biggest is odd
                    innerSquareHeight = innerSquareLength - 1;
                }
            }else{
                if(height%2==0){
                    //the smallest is odd and the biggest is even
                    innerSquareHeight = innerSquareLength + 1;
                }else{
                    //dimensions are both odd or equal
                    innerSquareHeight = innerSquareLength;
                }
            }
        }
        else{
            innerSquareHeight = height - 2;
            if(length%2==0){
                if(height%2==0){
                    //dimensions are both even
                    innerSquareLength = innerSquareHeight;
                }else{
                    //the smallest is odd and the biggest is even
                    innerSquareLength = innerSquareHeight + 1;
                }
            }else{
                if(height%2==0){
                    //the smallest is even and the biggest is odd
                    innerSquareLength = innerSquareHeight - 1;
                }else{
                    //dimensions are both odd
                    innerSquareLength = innerSquareHeight;
                }
            }
        }
    }
    private void setEnvironment(){
        //iff karel starts from the lower left corner
        setLength();
        setHeight();
        setInnerSquareDimensions();
        setBeepersInBag(1000);
    }
    private boolean mapCanBeDivided(){
        //the minimum map size that the map can be
        // divided based on it, is 7, the dimensions
        // should be at least 7
        return length >= 7 && height >= 7;
    }
    private void moveToTheMiddle(int wallLength)	{
        int movements = wallLength%2==0 ? (wallLength/2)-1 : (wallLength/2);
        for(int i=0; i<movements; i++){
            move();
        }
        turnLeft();//the movement is counterclockwise
    }
    private void moveToTheWall(){
        while(frontIsClear()){
            move();
        }
    }
    private void drawAnEdge(int length){
        for(int i=0; i<length; i++){
            move();
            putBeeper();
        }
    }
    private void drawALine(){
        while(frontIsClear()){
            move();
            putBeeper();
        }
    }
    private void drawSquareEdges(){
        drawAnEdge(innerSquareHeight-1);
        turnLeft();
        drawAnEdge(innerSquareLength-1);
        turnLeft();
        drawAnEdge(innerSquareHeight-1);
        turnLeft();
    }
    private void goAndBack(int length){
        drawALine();
        turnAround();
        for(int i=0; i<length / 2 - 1; i++){
            move();
        }
        turnLeft();
    }
    private void drawOuterChamberDoubleLines(int length){
        drawALine();
        turnRight();
        move();
        putBeeper();
        turnRight();
        drawAnEdge(length - 1);
        turnLeft();
    }
    private void draw(){
        //when draw an edge, specify the right length for each one.
        //4 cases:
        //(1) odd length for height, odd length for width
        //(2) odd length for height, even length for width
        //(3) even length for height, odd length for width
        //(4) even length for height, even length for width

        //true for even and odd lengths
        moveToTheMiddle(length);
        putBeeper();
        drawAnEdge((height - innerSquareHeight)/2);
        turnRight();
        //true for even and odd
        drawAnEdge(innerSquareLength/2);
        turnLeft();
        drawSquareEdges();
        if(innerSquareLength%2==0)
            drawAnEdge(innerSquareLength/2 - 1);
        else
            drawAnEdge(innerSquareLength/2);
        turnLeft();
        //(1) odd length for height, odd length for width
        if(length %2==0){
            if(height %2==0){
                drawAnEdge(innerSquareHeight / 2 - 1);
                turnLeft();
                drawOuterChamberDoubleLines(length/2);
                drawOuterChamberDoubleLines(height/2);
                drawOuterChamberDoubleLines(length/2);
                drawALine();
                turnRight();
            }
            //(2) odd length for height, even length for width
            else{
                if(length/2-1 <= height/2) {
                    drawAnEdge(innerSquareHeight / 2);
                    turnLeft();
                    goAndBack(length);
                    drawOuterChamberDoubleLines(height / 2 + 1);
                    goAndBack(length);
                    drawALine();
                } else {
                    drawOuterChamberDoubleLines(height);
                    moveToTheWall();
                    turnLeft();
                    moveToTheMiddle(height);
                    putBeeper();
                    drawALine();
                }
            }
        }
        //(3) even length for height, odd length for width
        else{
            if(height %2==0){
                drawALine();
                turnAround();
                moveToTheMiddle(height);
                drawOuterChamberDoubleLines(length/2);
                turnRight();
                move();
                drawOuterChamberDoubleLines(length/2);
            }
            //(4) even length for height, even length for width
            else{
                drawALine();
                turnLeft();
                moveToTheWall();
                turnLeft();
                moveToTheMiddle(height);
                putBeeper();
                drawALine();

            }
        }
    }

    public void run() {

        setEnvironment();

        if (mapCanBeDivided()) {
            draw();
        } else {
            System.out.println("This World is too small," +
                    " it can't be divided!");
        }
    }
}
