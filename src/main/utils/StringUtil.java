package main.utils;

import java.util.ArrayList;
import java.util.Collection;

public class StringUtil {

    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }


    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }


    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * 归并排序
     * TODO Test ArrayList 是引用类型 所以可能不需要返回值? 还没有测试过.
     * 备注:ArrayList可以直接用sort排序
     * @param list
     * @return
     */
    public static ArrayList<String> mergeSort( ArrayList<String> list){
        int listSize = list.size();
        ArrayList<String> temp =new ArrayList();
        var returnlist = sort(list,0, listSize-1,temp);
        return  returnlist;
    }
    /**
     * 归并排序-拆分
     * @param list
     * @param left
     * @param right
     * @param temp
     * @return
     */
    private static ArrayList<String> sort( ArrayList<String> list, int left,int right,ArrayList<String> temp){
        if(left<right){
            int mid = (left+right) / 2;
            list = sort(list,left,mid,temp);           //左边归并排序，使得左子序列有序
            list = sort(list,mid+1,right,temp);    //右边归并排序，使得右子序列有序
            list = merge(list,left,mid,right,temp);    //将两个有序子数组合并操作
        }
        return  list;
    }
    /**
     * 归并排序: 合并
     * @param list
     * @param left
     * @param mid
     * @param right
     * @param temp
     * @return
     */
    private static ArrayList<String> merge( ArrayList<String> list, int left,int mid, int right,ArrayList<String> temp){
        int i = left;//左序列指针
        int j = mid+1;//右序列指针
        int t = 0;//临时数组指针
        while (i<=mid && j<=right){
            if(list.get(i).compareTo(list.get(j)) <= 0){
                temp.set(t,list.get(i));
                t = t + 1;
                i = i + 1;
            }else {
                temp.set(t,list.get(j));
                t = t + 1;
                j = j + 1;
            }
        }
        while(i<=mid){              //将左边剩余元素填充进temp中
            temp.set(t,list.get(i));
            t = t + 1;
            i = i + 1;
        }
        while(j<=right){            //将右序列剩余元素填充进temp中
            temp.set(t,list.get(j));
            t = t + 1;
            j = j + 1;
        }
        t = 0;
        //将temp中的元素全部拷贝到原数组中
        while(left <= right){
            list.set(left,temp.get(t));
            left ++;
            t++;
        }
        return list;
    }

}
