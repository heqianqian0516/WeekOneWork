package bean;

public class EventBean {
    private String name;
    private Object clazz;
    private int num;

    public EventBean(String name, Object clazz) {
        this.name = name;
        this.clazz = clazz;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getClazz() {
        return clazz;
    }

    public void setClazz(Object clazz) {
        this.clazz = clazz;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
