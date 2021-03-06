package com.company;

public class Main {

    public static void main(String[] args) {
        Shape shape = new Cylinder();  // אפשרי בגלל הירושה
        Rollable rollable = new Ball();
        // rollable.bounce // א"א להפעיל אותה למרות שיש לו את המתודה (עד שנעשה המרה)
        Drawable[] drawables;

        Point[] myPoint={new Point(98,22),new Point(4,5),new Point(2,7),new Point(7,2)};
        bubleSort(myPoint);

        for (int i = 0; i <myPoint.length ; i++) {
            System.out.print(myPoint[i]);
        }





        MotionSensor sensor = new MotionSensor();
	    //Siren siren = new Siren();
	    //sensor.setListener(siren);
        Police police = new Police();
        sensor.setListener(police);
	    sensor.detectMotion();


	    Button btnLogin = new Button();
	    btnLogin.setListener(new Button.OnClickListener() {    //נתנו לו כתובת לאובייקט שאם יממשו את הפונקציה הוא כשיר לקבל את העדכונים - סיפקנו לו קטע קוד של מהלעשות כשלוחצים עליו
            @Override
            public void onClick(View view) {
                System.out.println("button clicked");
            }
        });




       List list =new LinkedList();  // אפשר גם כך או כמו למטה

        LinkedList linkedList=new LinkedList();
        linkedList.add(5);
        linkedList.add(3);
        linkedList.add(4);
        linkedList.add(7);
        linkedList.add(100,1);
        linkedList.remove(3);
        System.out.println(linkedList);


       /* זה גרוע מאוד ולכן נשתמש ב Iterator והשני
       int size=linkedList.setSize();
        int max=linkedList.get(0);
        for (int i = 1; i <size ; i++) {
            int vale=linkedList.get(i);
            if (vale>max)
                max=vale;
        }
        System.out.println(max);
*/
        // ווכה אנו עוברים בצורה הטובה
        //int max=linkedList.get(0);
        for (Object i:linkedList) {
            Integer x=(Integer)i;
            System.out.println();
        }

        // זה התרגום של השורות למעלה  -ה foreach
        /*linkedList.iterator();
        while (linkedList.hasNext()){
            Integer x=(Integer)linkedList.next();
            System.out.println(x);

        }
*/


        Dog myDog =Dog.getDog();
        myDog.setName("snoopy");

        Dog anotherDog= Dog.getDog();
        System.out.println(anotherDog.getName());




        mergeSortLinkedList.Node list1 =
                new mergeSortLinkedList.Node(17,
                        new mergeSortLinkedList.Node(4,
                                new mergeSortLinkedList.Node(1,
                                        new mergeSortLinkedList.Node(12))));



        mergeSortLinkedList.Node list2 =
                new mergeSortLinkedList.Node(5,
                        new mergeSortLinkedList.Node(6,
                                new mergeSortLinkedList.Node(20)));
        mergeSortLinkedList.Node merged = mergeSortLinkedList.merge(list1, list2);
        while(merged != null){
            System.out.print(merged.value + " ");
            merged = merged.next;
        }

        mergeSortLinkedList.Node sorted = mergeSortLinkedList.mergeSort(list1);
        while(sorted != null){
            System.out.print(sorted.value + " ");
            sorted = sorted.next;
        }





    }
    //Comparable התאמנו ל
        static void bubleSort(Comparable[] arr) {
        int upTo = arr.length - 1; // האיבר האחרון
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int i = 0; i < upTo; i++) {
                if (arr[i].compareTo(arr[i + 1])>0) {
                    Comparable temp = arr[i];
                    arr[i] = arr[i +  1];
                    arr[i + 1] = temp;
                    isSorted = false;
                }
            }
            upTo--; // נעזוב את האיבר האחרון כי הוא במקומו
        }
    }
}


interface Rollable{
    void roll(float gegree);
}

interface Drawable{
    void draw(boolean[][]canvas);
}

class Ball implements Rollable{

    @Override
    public void roll(float gegree) {

        }
    public void bounce(float gegree) {}

}

class Shape implements Drawable{
    @Override
    public void draw(boolean[][] canvas) {

    }
}

class Circle implements Drawable,Rollable{
    @Override
    public void roll(float gegree) {

    }

    @Override
    public void draw(boolean[][] canvas) {

    }
}

class Cylinder extends Shape implements Rollable{

    @Override
    public void roll(float gegree) {

    }
}

interface Edible{
    void eat(String food);
}



//Listener Model

/*class MotionSensor{
    private MotionListener listeners;
    public void setListener(MotionListener listeners) {
        this.listeners = listeners;
    }
    void detectMotion(){
        if(listeners != null)
            listeners.motionDetected(123);
    }
    interface MotionListener{
        void motionDetected(int sensorId);
    }
}*/

// לכמה listner
class MotionSensor{

    private MotionListener[] listeners;
    int size;

    public MotionSensor(){
        listeners = new MotionListener[10];
        size = 0;
    }

    public void setListener(MotionListener listener) {
        if(listener == null)
            return;
        if(size < this.listeners.length)
            this.listeners[size++] = listener;
    }

    //הוא תכלס יודע לגלות תנועה
    void detectMotion(){
        for (int i = 0; i < size; i++) {
            this.listeners[i].motionDetected(123);
        }

    }

    interface MotionListener{
        void motionDetected(int sensorId);
    }

}

class Siren implements MotionSensor.MotionListener{

    @Override
    public void motionDetected(int sensorId) {
        System.out.println("alarm!!! motion detected at sensor " +
                sensorId);

    }
}


class Police implements MotionSensor.MotionListener{

    @Override
    public void motionDetected(int sensorId) {
        System.out.println("stop! you are under arrest...");
    }
}


class View{

}


class Button extends View{

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }


    void detectClick(){
        if(listener != null)
            listener.onClick(this);
    }

    interface OnClickListener{
        void onClick(View view);
    }
}




//Singelton
class Dog{
    private static Dog dog;
    String name;
    private Dog (){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // המתודה הסטטית הזו, אם זה null כבר לא יהיה. אם לא זה פשוט מחזיר את הכתובת של האובייקט
    public static Dog getDog(){
        if (dog==null)
            dog=new Dog();
        return dog;
    }
}




















