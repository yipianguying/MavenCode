// package junit.test;
//
// import com.crowd.funding.manager.bean.User;
// import com.crowd.funding.manager.service.UserService;
// import com.crowd.funding.utils.MD5Utils;
// import org.springframework.context.ApplicationContext;
// import org.springframework.context.support.ClassPathXmlApplicationContext;
//
// public class Test04 {
//     public static void main(String[] args) {
//         ApplicationContext ioc = new ClassPathXmlApplicationContext("spring/spring*.xml");
//
//         UserService userService = ioc.getBean(UserService.class);
//
//
//         for (int i = 1; i <= 100; i++) {
//             User user = new User();
//             user.setLoginacct("test"+i);
//             user.setUserpswd(MD5Utils.digest("123"));
//             user.setUsername("test"+i);
//             user.setEmail("test"+i+"@163.com");
//             user.setCreatetime("2021-02-23 14:17:00");
//             userService.saveUser(user);
//         }
//     }
// }
