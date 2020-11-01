# KLIS

https://algospot.com/judge/problem/read/KLIS

# 구현 방법
```
 i)   일차원 cache배열을 이용하여 일단 각 위치별로 만들 수 있는 LIS의 길이를 저장해둔다. 이 값은 재귀함수를 통해 구한다.
      ex) 1 9 7 4 2 6 3 11 10 가 입력으로 들어오면 4 2 2 3 3 2 2 1 1 이 저장이 된다.
    
 ii)  이제 k번째 LIS를 구한다. k번째 LIS를 구하는 방법은 k-1번째까지의 LIS를 무시하고 지나가는 것이다. 아래와 같은 방법으로 구현한다.
 
 iii) 각 위치별로 만들 수 있는 LIS의 개수를 구해야 한다. 예를 들어 i)의 예제와 같은 입력이 들어왔을 때 1로 시작하는 LIS의 개수는 6이 된다.
      개수를 구했을 때 이게 만약 k-1보다 작거나 같은 경우 이것으로부터 시작하는 LIS는 모두 무시해도 된다는 의미이다.
      
 iv) 위와 같은 방법으로 불필요한 탐색은 무시하며 k번째 LIS를 찾고 출력한다.
 
 v) 9 2
    1 9 7 4 2 6 3 11 10 과 같은 입력이 들어왔을 때
 
    수열     1 9 7 4 2 6 3 11 10
    LIS길이  4 2 2 3 3 2 2  1  1
    LIS개수  6     2 4 2 2  1  1  와 같이 된다. 
 
    1) 1에서 만들 수 있는 LIS의 개수는 6개. 무시할 수 있는 LIS의 개수는 1개이므로 2번째 LIS는 1로 시작한다.
    2) 1 -> 2 로 만들 수 있는 LIS의 개수는 4개. 무시할 수 있는 LIS의 개수는 1개이므로 2번째 LIS는 1->2 로 시작한다.
    3) 1 -> 2 -> 3 으로 만들 수 있는 LIS의 개수는 2개. 무시할 수 있는 LIS의 개수는 1개이므로 2번째 LIS는 1->2->3으로 시작한다.
    4) 1 -> 2 -> 3 -> 10 으로 만들 수 있는 LIS의 개수는 1개. 무시할 수 있는 LIS의 개수는 1개이므로 이것은 무시가 가능하다.
    5) 1 -> 2 -> 3 -> 11 이 2번째 LIS가 된다.
    
    이렇게 불필요한 탐색 과정을 줄인다.
 ```
 
 # 구현 코드
 ```java
import java.util.Arrays;
import java.util.Scanner;

public class KLIS {
	
	static int[] cacheLen = new int[501];
	static int[] cacheCnt = new int[501];
	static int[] lis_r = new int[501];
	static int lis_p;
	static int MAX = 2000000000 + 1;
	
	public static void main(String args[])
	{
	
		Scanner sc = new Scanner(System.in);
		
		int c = sc.nextInt();
		
		for(int i = 0; i < c; i++)
		{
			int n = sc.nextInt();
			int k = sc.nextInt();
			int[] sq = new int[n];
			
			int max = 0;
			
			for(int j = 0; j < n; j++)
			{
				sq[j] = sc.nextInt();
			}
			
			Arrays.fill(cacheLen, -1);
			Arrays.fill(cacheCnt, -1);
			
			max = lis(-1,sq);
			System.out.println(max - 1);
			
			int skip = k - 1;
			
			lis_p = 0;
			
			reconstruct(-1, skip, sq);
			
			for(int j = 0; j < lis_p; j++)
			{
				System.out.print(lis_r[j]+" ");
			}
			System.out.println();
		}
	}
	
	public static int lis(int start, int[] s)
	{
		if(cacheLen[start + 1] != -1) return cacheLen[start + 1];
		
		cacheLen[start + 1] = 1;
		
		for(int i = start + 1; i < s.length; i++)
		{
			if( start == -1 || s[start] < s[i] )
				cacheLen[start + 1] = Math.max(cacheLen[start + 1], lis(i,s) + 1);
		}
		
		return cacheLen[start + 1];
	}
	
	public static int count(int start, int[] s)
	{
		if(lis(start, s) == 1) return 1;
		
		if(cacheCnt[start + 1] != -1) return cacheCnt[start + 1];
		
		cacheCnt[start + 1] = 0;
		
		for(int i = start + 1; i < s.length; i++)
		{
			if((start == -1 || s[start] < s[i]) && lis(start, s) == lis(i,s) + 1)
			{
				cacheCnt[start + 1] = (int)Math.min(MAX, (long)cacheCnt[start + 1] + count(i,s));
			}
		}
		
		return cacheCnt[start + 1];
	}
	
	public static void reconstruct(int start, int skip, int[] s)
	{
		if(start != -1) lis_r[lis_p++] = s[start];
	
		int[][] followers = new int[500][2];
		int followers_p = 0;
		
		for(int i = start + 1; i < s.length; i++)
		{
			if((start == -1 || s[start] < s[i]) && lis(start,s) == lis(i,s) + 1)
			{
				followers[followers_p][0] = s[i];
				followers[followers_p++][1] = i;
			}
		}
		
		Arrays.sort(followers,0,followers_p,(o1,o2) -> {
			if(o1[0] == o2[0])
			{
				return Integer.compare(o1[1],o2[1]);
			}
			else
			{
				return Integer.compare(o1[0],o2[0]);
			}
		});
		
		for(int i = 0; i < followers_p; i++)
		{
			int idx = followers[i][1];
			int cnt = count(idx,s);
			if(cnt <= skip)
				skip -= cnt;
			else
			{
				reconstruct(idx, skip, s);
				break;
			}
		}
	}
}
```
