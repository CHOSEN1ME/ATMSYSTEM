import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATMSystem {
    public static void main(String[] args) {
//        1.准备系统需要的容器
        ArrayList<Account> accounts = new ArrayList<>();
//        2.系统界面
        showMain(accounts);
    }

    public static void showMain(ArrayList<Account> accounts) {
        System.out.println("=====欢迎进入首页=====");
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入想做的操作");
            System.out.println("1.登录");
            System.out.println("2.开户");
            System.out.println("输入操作（1 or 2）");
            int commond = sc.nextInt();
            switch (commond) {
                case 1:
                    login(accounts, sc);
                    break;
                case 2:
                    register(accounts, sc);
                    break;
                default:
                    System.out.println("输入有误");
            }

        }

    }

    public static void register(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("======用户开户功能页面======");
        System.out.println("请输入开户名称");
        String name = sc.next();
        String password = "";
        while (true) {
            System.out.println("请输入开户密码");
            password = sc.next();
            System.out.println("请输入确认密码");
            String okPassword = sc.next();
//            判断两次密码是否一致
            if (okPassword.equals(password)) {
                break;
            } else System.out.println("两次密码不一致");
        }
        System.out.println("请输入当次限额");
        double quoteMoney = sc.nextDouble();
//        3.生成账户的卡号，卡号是8位，而且不能重复卡号
        String cardId = createCarId(accounts);
//        4.创建一个账户对象封装账户的信息
        Account account = new Account(cardId, name, password, quoteMoney);
//        5.把账户对象添加到集合里
        accounts.add(account);
        System.out.println("开户成功！卡号是：" + account.getCardId());
    }

    public static String createCarId(ArrayList<Account> accounts) {
        while (true) {
            String carId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                carId += r.nextInt(10);
            }
//            判断卡号是否存在
            Account acc = getAccountById(carId, accounts);
            if (acc == null) {
                return carId;
            }
        }
    }

    private static Account getAccountById(String carId, ArrayList<Account> accounts) {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account.getCardId().equals(carId)) {
//                说明存在，那么返回存在的卡号
                return account;
            }

        }
        return null;
    }

    public static void login(ArrayList accounts, Scanner sc) {
        if (accounts.size() == 0) {
            System.out.println("当前系统中无任何账户，需要先注册");
            return; //直接结束方法的执行
        }
        while (true) {
            //让用户输入卡号，根据卡号查询账户对象
            System.out.println("请输入登录的卡号：");
            String carId = sc.next();
            Account acc = getAccountById(carId, accounts);
            if (acc != null) {
                while (true) {
//                    4.让用户继续输入密码
                    System.out.println("请输入登录的密码");
                    String password = sc.next();
//                    5.判断密码是否正确
                    if (acc.getPassWord().equals(password)) {
                        System.out.println("恭喜" + acc.getUserName() +
                                "先生/女生成功进入系统你的卡号是" + acc.getCardId());
                        //展示系统登录后的操作界面
                        showUserCommand(sc, acc, accounts);
                        return;//结束登录方法
                    } else System.out.println("密码输入有误，请重新输入！");
                }
            } else System.out.println("不存该卡号用户");
        }
    }

    private static void showUserCommand(Scanner sc, Account acc, ArrayList accounts) {
//        用户登录后操作界面
        while (true) {
            System.out.println("=======用户操作界面=======");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出");
            System.out.println("7.注销账户");
            System.out.println("请输入操作命令");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    //查询账户
                    showAccount(acc);
                    break;
                case 2:
                    //存款
                    depositMoney(acc, sc);
                    break;
                case 3:
                    //取款
                    drawMoney(acc, sc);
                    break;
                case 4:
                    //转账
                    transferMoney(accounts, acc, sc);
                case 5:
                    //修改密码
                    updatePassword(acc, sc);
                    return;
                case 6:
                    //退出
                    System.out.println("");
                case 7:
                    //注销账户
                    accounts.remove(acc);
                    System.out.println("销户成功！");
                    return;
                default:
                    System.out.println("输入命令有误，请重新输入");
            }

        }
    }

    private static void updatePassword(Account acc, Scanner sc) {
        System.out.println("======修改密码======");
        while (true) {
            System.out.println("请输入正确的密码：");
            String okPassword = sc.next();
            if (acc.getPassWord().equals(okPassword)) {
                while (true) {
                    System.out.println("请输入新的密码：");
                    String newPassword = sc.next();
                    System.out.println("请输入确认密码：");
                    String okNewPassword = sc.next();
                    if (newPassword.equals(okNewPassword)) {
                        acc.setPassWord(newPassword);
                        System.out.println("密码修改成功！");
                        return;
                    } else System.out.println("两次密码输入不一致");
                }
            } else System.out.println("当前输入正确密码错误！");

        }

    }

    private static void transferMoney(ArrayList accounts, Account acc, Scanner sc) {
//        1.判断系统中是否有两个以上账户
        if (accounts.size() < 2) {
            System.out.println("对不起，该系统中无其他账户，不可以转账！");
            return;
        }
//        2.判断自己的账户对象是否有钱
        if (acc.getMoney() == 0) {
            System.out.println("对不起，该账户余额为0，不支持转账！");
            return;
        }
//        3.开始转账
        while (true) {
            System.out.println("请输入对方账户的卡号");
            String cardId = sc.next();
            Account account = getAccountById(cardId, accounts);
            //判断这个账户对象是否是当前登录的账户对象
            if (account != null) {
                if (account.getCardId().equals(acc.getCardId())) {
                    //正在给自己转账
                    System.out.println("不可以给自己转账！");
                } else {
                    //  确认对方姓氏
                    String name = "*" + acc.getUserName().substring(1);

                    System.out.println("请确认" + name + "的姓氏");
                    String preName = sc.next();
                    //判断输入的姓氏是否正确
                    if (account.getUserName().startsWith(preName)) {
                        System.out.println("请输入转账的金额");
                        double money = sc.nextDouble();
                        //判断这个金额是否超过自己余额
                        if (money > acc.getMoney()) {
                            System.out.println("转账金额过多，您做多可以转账" + acc.getMoney());
                        } else {
                            //开始转账
                            acc.setMoney(acc.getMoney() - money);
                            account.setMoney(acc.getMoney() + money);
                            System.out.println("转账成功!已经为" + account.getUserName() + "转账了" + money);
                            //转账完查看自己账户信息
                            showAccount(acc);
                        }

                    } else System.out.println("认证信息有误！");

                }
            } else System.out.println("对不起输入转账卡号有误！");
        }

    }

    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("=======取钱操作=======");
//        1.判断账户是否大于100
        if (acc.getMoney() >= 100) {
            while (true) {
                System.out.println("请输入取款金额");
                double money = sc.nextDouble();
                if (acc.getQuotaMoney() <= money) {
                    System.out.println("当前取款超过每次限额，每次最多取：" + acc.getQuotaMoney());

                } else {
                    if (acc.getMoney() >= money) {
                        acc.setMoney(acc.getMoney() - money);
                        System.out.println("取钱" + money + "成功！当前账户余额：" + acc.getMoney());
                        return;
                    } else System.out.println("余额不足！");
                }
            }
        }

    }

    private static void depositMoney(Account acc, Scanner sc) {
        System.out.println("=======存钱操作=======");
        System.out.println("请输入存款的金额");
        double money = sc.nextDouble();
        //直接把金额修改到账户中的money属性
        acc.setMoney(acc.getMoney() + money);
        System.out.println("存款成功！");
        showAccount(acc);
    }

    private static void showAccount(Account acc) {
        System.out.println("=======当前账户详情=======");
        System.out.println("卡号：" + acc.getCardId());
        System.out.println("姓名：" + acc.getUserName());
        System.out.println("余额：" + acc.getMoney());
        System.out.println("当次限额:" + acc.getQuotaMoney());
    }

}
