package bike.myapplication;

public interface Constants { //블루투스 상태를 나타내기 위한 연결 페이지
    public static final int MESSAGE_STATE_CHANGE = 1;
    //메세지 상태 변화 --블루투스에서
    public static final int MESSAGE_READ = 2;
    //메세지에서 읽어오는 상태 --블루투스에서
    public static final int MESSAGE_WRITE = 3;
    //메세지에서 쓰기위해 만드는 상태--블루투스 에서
    public static final int MESSAGE_DEVICE_NAME = 4;
    //메세지에서 블루투스의 장치이름 받아오기
    public static final int MESSAGE_TOAST = 5;
    //메세지로 상태를 보여주는 단계
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    //디바이스 이름과 상태를 설정해주기 위해서 설정함

}
//인터베이스 액티비디 더 있으면 거기에 또 넣어야 하니까 .... 이렇게 설정함