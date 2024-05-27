package com.sparta;

public class RamenProgram {
    public static void main(String[] args) {
        try {
            // 메인 메서드 매개변수로 부터 라면의 개수를 입력받아 RamenCook 인스턴스 생성
            RamenCook ramenCook = new RamenCook(Integer.parseInt(args[0]));

            // 이름이 A,B,C,D Thread를 시작시킨다.
            new Thread(ramenCook, "A").start();
            new Thread(ramenCook, "B").start();
            new Thread(ramenCook, "C").start();
            new Thread(ramenCook, "D").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RamenCook implements Runnable {
    private int ramenCount;
    private String[] burners = {"_", "_", "_", "_"}; // 4개의 버터 상태를 나타내는 배열 (가용상태 : "_", 사용중 : "A","B","C","D")

    // 생성자로 라면의 개수를 입력받아 설정한다.
    public RamenCook(int ramenCount) {
        this.ramenCount = ramenCount;
    }

    @Override
    public void run() { //각 Thread 실행하는 메서드
        //ramenCount가 0이 될때 까지 반복
        while (ramenCount > 0){

            // 한 Thread가 ramenCount를 사용하는 동안 다른 Thread의 ramenCount 접근을 막늗다.(ramenCount의 수정을 막는다.)
            synchronized (this) {
                //라면의 개수를 감소시키고 남은 라면의 갯수 출력
                ramenCount--;
                System.out.println(
                        Thread.currentThread().getName() + ": " + ramenCount + "개 남음"
                );
            }

            //가용한 버너를 찾기 위한 반복문
            for(int i=0; i< burners.length; i++){
                // i번째 버너가 사용가능한 상태("_")가 아니면 다음으로 넘긴다.
                if(!burners[i].equals("_")) continue;

                // 한 Thread에서 burners[i]를 사용하는 동안 다른 Thread의 burners[i]의 접근을 막는다.(burners[i]의 수정을 막는다.)
                synchronized (this){
                    // i번째 버너를 현재 실행중인 Thread의 이름으로 설정 ("A" or "B" or "C" or "D") 그리고 해당
                    burners[i] = Thread.currentThread().getName();
                    //현재 i번째 버너를 사용함(버너 ON)을 출력
                    System.out.println(
                            "           " + Thread.currentThread().getName() + ": [" + (i + 1) + "]번 버너 ON"
                    );
                    showBurners();
                }

                try{
                    // 2초 동안 기다린다.
                    Thread.sleep(2000);
                }catch(Exception e){
                    e.printStackTrace();
                }

                synchronized (this){
                    // i 번째 버너의 상태를 "_" 사용 가능한 상태("_")로 변경
                    burners[i] = "_";
                    // i 번째 버너를 다 사용했음(버너 OFF)을 출력
                    System.out.println(
                            "            " + Thread.currentThread().getName() + ": [" + (i + 1) + "]번 버터 OFF"
                    );
                    showBurners();
                }
                break;
            }

            try {
                // Math.random() * 1000으로 0이상 1000미만의 실수가 생성
                // round로 소수 첫번쨰 자리에서 반올림
                // 그만큼 Thread를 Sleep시킨다.
                Thread.sleep(Math.round(1000 * Math.random()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //버너들의 현재 상태를 출력하는 메서드
    private void showBurners() {
        String stringToPrint = "                      ";

        for (int i = 0; i < burners.length; i++) {
            stringToPrint += (" " + burners[i]);
        }
        System.out.println(stringToPrint);
    }
}
