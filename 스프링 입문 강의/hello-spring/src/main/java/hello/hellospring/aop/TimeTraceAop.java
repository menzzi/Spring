package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component //빈으로 직접 등록이 좋으나 여기선 컴포넌트 스캔함.
public class TimeTraceAop {

    @Around("execution(* hello.hellospring..*(..))")
    public Object execute (ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        System.out.println("START: "+joinPoint.toString());
        try{
            //다음 메서드로 진행
            return joinPoint.proceed();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: "+joinPoint.toString()+" "+ timeMs + "ms");

        }
    }
}
