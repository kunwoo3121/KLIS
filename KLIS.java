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