package hello.core.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();
    //static 으로 선언해서 class 레벨에 딱 하나만 올라감

    public static SingletonService getInstance(){
        return instance;
    }

    private SingletonService(){

    }

    public void logic(){
        System.out.println("싱글톤 객체 로직 호출");
    }
}
