package com.babel.common.core.performance;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;  
/** *//**  
 * 获取系统信息的业务逻辑实现类.  
 * @author amg * @version 1.0 Creation date: 2008-3-11 - 上午10:06:06  
 */  
public class MonitorUtil {
	 //可以设置长些，防止读到运行此次系统检查时的cpu占用率，就不准了   
    private static final int CPUTIME = 2000;   
  
    private static final int PERCENT = 100;   
  
    private static final int FAULTLENGTH = 10;   
  
    /** *//**  
     * 获得当前的监控对象.  
     * @return 返回构造好的监控对象  
     * @throws Exception  
     * @author amg     * Creation date: 2008-4-25 - 上午10:45:08  
     */  
    public MonitorInfoBean getMonitorInfoBean() throws Exception {   
        int kb = 1024;   
           
        // 可使用内存   
        long totalMemory = Runtime.getRuntime().totalMemory() / kb;   
        // 剩余内存   
        long freeMemory = Runtime.getRuntime().freeMemory() / kb;   
        // 最大可使用内存   
        long maxMemory = Runtime.getRuntime().maxMemory() / kb;   
        // 已使用的内存   
        long usedMemory = (totalMemory - freeMemory);   
  
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory   
                .getOperatingSystemMXBean();   
  
        // 操作系统   
        String osName = System.getProperty("os.name");   
        // 总的物理内存   
        long totalPhysicalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;   
        // 剩余的物理内存   
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;   
        
  
        // 获得线程总数   
        ThreadGroup parentThread;   
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread   
                .getParent() != null; parentThread = parentThread.getParent())   
            ;   
        int totalThread = parentThread.activeCount();   
        
        // 构造返回对象   
        MonitorInfoBean infoBean = new MonitorInfoBean();   
  
//        double cpuRatio = 0;   
//        if (osName.toLowerCase().startsWith("windows")) {   
//            cpuRatio = this.getCpuRatioForWindows();   
//        }   
//        infoBean.setCpuRatio(cpuRatio);
           
       
        infoBean.setFreeMemory(freeMemory);   
        infoBean.setTotalMemory(totalMemory);   
        infoBean.setUsedMemory(usedMemory);   
        
        infoBean.setMaxMemory(maxMemory);   
        infoBean.setOsName(osName);   
        infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);   
        infoBean.setTotalPhysicalMemorySize(totalPhysicalMemorySize);
        infoBean.setTotalThread(totalThread);   
       
   
        return infoBean;   
    }   
  
    /** *//**  
     * 获得CPU使用率.  
     * @return 返回cpu使用率  
     * @author amg     * Creation date: 2008-4-25 - 下午06:05:11  
     */  
    private double getCpuRatioForWindows() {   
        try {   
            String procCmd = System.getenv("windir")   
                    + "//system32//wbem//wmic.exe process get Caption,CommandLine,"  
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";   
            // 取进程信息   
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));   
            Thread.sleep(CPUTIME);   
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));   
            if (c0 != null && c1 != null) {   
                long idletime = c1[0] - c0[0];   
                long busytime = c1[1] - c0[1];   
                return Double.valueOf(   
                        PERCENT * (busytime) / (busytime + idletime))   
                        .doubleValue();   
            } else {   
                return 0.0;   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
            return 0.0;   
        }   
    }   
  
    /** *//**  
     * 读取CPU信息.  
     * @param proc  
     * @return  
     * @author amg     * Creation date: 2008-4-25 - 下午06:10:14  
     */  
    private long[] readCpu(final Process proc) {   
        long[] retn = new long[2];   
        try {   
            proc.getOutputStream().close();   
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());   
            LineNumberReader input = new LineNumberReader(ir);   
            String line = input.readLine();   
            if (line == null || line.length() < FAULTLENGTH) {   
                return null;   
            }   
            int capidx = line.indexOf("Caption");   
            int cmdidx = line.indexOf("CommandLine");   
            int rocidx = line.indexOf("ReadOperationCount");   
            int umtidx = line.indexOf("UserModeTime");   
            int kmtidx = line.indexOf("KernelModeTime");   
            int wocidx = line.indexOf("WriteOperationCount");   
            long idletime = 0;   
            long kneltime = 0;   
            long usertime = 0;   
            while ((line = input.readLine()) != null) {   
                if (line.length() < wocidx) {   
                    continue;   
                }   
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,   
                // ThreadCount,UserModeTime,WriteOperation   
                String caption = Bytes.substring(line, capidx, cmdidx - 1)   
                        .trim();   
                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();   
                if (cmd.indexOf("wmic.exe") >= 0) {   
                    continue;   
                }   
                // log.info("line="+line);   
                if (caption.equals("System Idle Process")   
                        || caption.equals("System")) {   
                    idletime += Long.valueOf(   
                            Bytes.substring(line, kmtidx, rocidx - 1).trim())   
                            .longValue();   
                    idletime += Long.valueOf(   
                            Bytes.substring(line, umtidx, wocidx - 1).trim())   
                            .longValue();   
                    continue;   
                }   
  
                kneltime += Long.valueOf(   
                        Bytes.substring(line, kmtidx, rocidx - 1).trim())   
                        .longValue();   
                usertime += Long.valueOf(   
                        Bytes.substring(line, umtidx, wocidx - 1).trim())   
                        .longValue();   
            }   
            retn[0] = idletime;   
            retn[1] = kneltime + usertime;   
            return retn;   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        } finally {   
            try {   
                proc.getInputStream().close();   
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
        }   
        return null;   
    }   
       
    /** *//**  
     * 测试方法.  
     * @param args  
     * @throws Exception  
     * @author amg     * Creation date: 2008-4-30 - 下午04:47:29  
     */  
    public static void main(String[] args) throws Exception {   
        MonitorUtil service = new MonitorUtil();   
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();   
        System.out.println("cpu占有率=" + monitorInfo.getCpuRatio());   
           
        System.out.println("可使用内存=" + monitorInfo.getTotalMemory()+ "kb");   
        System.out.println("剩余内存=" + monitorInfo.getFreeMemory()+ "kb");   
        System.out.println("最大可使用内存=" + monitorInfo.getMaxMemory()+ "kb");   
           
        System.out.println("操作系统=" + monitorInfo.getOsName());   
        System.out.println("总的物理内存	=" + monitorInfo.getTotalPhysicalMemorySize() + "kb"); 
        long usedMemory=monitorInfo.getTotalPhysicalMemorySize()-monitorInfo.getFreePhysicalMemorySize();
        System.out.println("剩余的物理内存	=" + monitorInfo.getFreePhysicalMemorySize() + "kb");   
        System.out.println("已使用的物理内存	=" + usedMemory + "kb");   
        System.out.println("线程总数=" + monitorInfo.getTotalThread());   
    }   
}

