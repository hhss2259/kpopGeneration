package kpop.kpopGeneration.dto;

public enum Category {

    ALL(0),MUSIC(1), REVIEW(2), CERTIFICATION(3), NORMAL(4), NEWS(5);

    private int value;

    Category(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }


}
