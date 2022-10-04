package com.lcb.nio;

import java.nio.IntBuffer;

/**
 *
   * @Author lcb
   * @Description Buffer 的使用
   * @Date 2022/2/11
   * @Param
   * @return
   **/
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer allocate = IntBuffer.allocate(5);

        //向buffer中存入数据
        for (int i = 0; i <allocate.capacity(); i++) {
            allocate.put(i*2);
        }

        //将buffer转换 读写转换
        allocate.flip();

        while (allocate.hasRemaining()){
            System.out.println(allocate.get());
        }
    }
}
