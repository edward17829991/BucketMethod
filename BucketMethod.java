
public class BucketMethod {
	
	public static int[] new_index(int[][] buckets,int[][] buckets_new){
		int[] new_index = new int[buckets.length];
		for(int i=0;i<buckets.length;i++){
			for(int j=0;j<buckets.length;j++){
				if(is_A_in_B(buckets[i],buckets_new[j])){
					new_index[i] = j;
					break;
				}
			}
		}
		return new_index;
	}
	
	public int[][] new_mapping(int[][] buckets){
		buckets = buckets_sorting(buckets);
		return return_the_best_solution(buckets,0);
	}
	
	private int[][] return_the_best_solution(int[][] buckets,int level){
		if(buckets.length==0||level<0) return null;
		else{
			int[][] allDistinctUnion = bucket_all_distinct_union(buckets);
			int[] evaluation_list = new int[allDistinctUnion.length];
			for(int i=0;i<evaluation_list.length;i++) 
				for(int j=0;j<buckets.length;j++) 
					if((is_A_in_B(buckets[j],allDistinctUnion[i]))) evaluation_list[i]++;
			int[] index_list = index_list_of_the_max(evaluation_list);
			int[][][] solution = new int[index_list.length][][];
			for(int i=0;i<index_list.length;i++){
				int[][] buckets_remain = annihilator(allDistinctUnion[index_list[i]],buckets);
				if(buckets_remain.length>0){					
					int[][] returned_solution = return_the_best_solution(buckets_remain,level+1);
					solution[i] = new int[returned_solution.length+1][];
					solution[i][0] = allDistinctUnion[index_list[i]];
					for(int j=0;j<returned_solution.length;j++) solution[i][j+1] = returned_solution[j];
				}
				else{
					solution[i] = new int[1][];
					solution[i][0] = allDistinctUnion[index_list[i]];
				}
			}
			int min = allDistinctUnion.length;
			int index_of_the_min = index_list.length;
			for(int i=0;i<solution.length;i++){				
				if(solution[i].length<=min){
					min = solution[i].length;
					index_of_the_min = i;
				}
			}
			return solution[index_of_the_min];
		}
	}
	
	public static int[][] annihilator(int[] solution,int[][] buckets){
		int count = 0;
		for(int i=0;i<buckets.length;i++)
			if(!is_A_in_B(buckets[i],solution)) count++;
		int[][] buckets_remain = new int[count][];
		count = 0;
		for(int i=0;i<buckets.length;i++)
			if(!is_A_in_B(buckets[i],solution)) buckets_remain[count++] = buckets[i];
		return buckets_remain;
	}
	
	public static int max_N(int[][] buckets){
		int max=0;
		for(int i=0;i<buckets.length;i++){
			if(buckets[i].length>=max) max=buckets[i].length;
		}
		return max;
	}
	
	public static int bucket_intersection_counter(int[] bucketA,int[] bucketB){
		int count=0;
		for(int valueA : bucketA) for(int valueB : bucketB) if(valueA==valueB) count++;
		return count;
	}
	
	public static int bucket_union_counter(int[] bucketA,int[] bucketB){
		return bucketA.length+bucketB.length-bucket_intersection_counter(bucketA,bucketB);
	}
	
	//兩資料資料融合器  回傳兩資料之聯集資料串
	public static int[] bucket_union(int[] bucketA,int[] bucketB){
		int[] newArray = new int[bucket_union_counter(bucketA,bucketB)];
		int a=0,b=0;
		for(int i=0;i<newArray.length;i++){
			if(a==bucketA.length){
				for(int j=i;j<newArray.length;j++){
					newArray[j]=bucketB[b];
					b++;
				}
				break;
			}
			else if(b==bucketB.length){
				for(int j=i;j<newArray.length;j++){
					newArray[j]=bucketA[a];
					a++;
				}
				break;
			}
			if(bucketA[a]<bucketB[b]){
				newArray[i]=bucketA[a];
				a++;
			}
			else if(bucketA[a]>bucketB[b]){
				newArray[i]=bucketB[b];
				b++;
			}
			else{
				newArray[i]=bucketA[a];
				a++;
				b++;
			}
		}
		return newArray;
	}
	
	//單資料串小至大排序
	public static int[] bucket_sorting(int[] bucket){
		for(int i=0;i<bucket.length;i++){
			for(int j=0;j<bucket.length;j++){
				if(bucket[i]<bucket[j]){
					int temp=bucket[j];
					bucket[j]=bucket[i];
					bucket[i]=temp;
				}
			}
		}
		return bucket;
	}
	
	//資料集合中所有資料串小至大排序
	public static int[][] buckets_sorting(int[][] buckets){
		for(int i=0;i<buckets.length;i++){
			buckets[i] = bucket_sorting(buckets[i]);
		}
		return buckets;
	}

	//資料集合中小於等於特定數目級聯集資料計數器
	public static int buckets_all_union_counter(int[][] buckets,int N){
		int count = 0;
		for (int y=0;y<buckets.length;y++){
			for (int x=0;x<=y;x++){
				if(bucket_union_counter(buckets[y],buckets[x]) <= N){
					count++;
				}
			}
		}
		return count;
	}
	
	//資料集合中特定數目級且非重複之聯集資料串之集合  回傳特定數量級非重複聯集資料串之集合
	public static int[][] bucket_all_distinct_union(int[][] buckets){
		int N = max_N(buckets);
		int numberOfUnion = buckets_all_union_counter(buckets,N);
		int distinct_solution_count = 0;
		int[][] distinct_solution = new int[numberOfUnion][N];
		for (int y=0;y<buckets.length;y++){
			for (int x=0;x<=y;x++){
				if(bucket_union_counter(buckets[y],buckets[x]) <= N){
					int[] new_solution = bucket_union(buckets[y],buckets[x]);
					if(distinct_solution_count==0) 
						distinct_solution[distinct_solution_count++] = new_solution;						
					else{
						boolean different = true;
						for(int i=0;i<distinct_solution_count;i++)
							if(new_solution==distinct_solution[i]){
								different = false;
								break;
							}
						if(different) distinct_solution[distinct_solution_count++] = new_solution;
					}
				}
			}
		}
		int[][] distinct_solution_new = new int[distinct_solution_count][N];
		for(int i=0;i<distinct_solution_count;i++) distinct_solution_new[i] = distinct_solution[i];
		return distinct_solution_new;
	}
		
	//資料串A是否包含於資料串B中  回傳布林值
	public static boolean is_A_in_B(int[] A,int[] B){
		int count = A.length;
		int i = 0;
		while(count>0&&i<A.length){
			for(int value : B) if(A[i]==value){
				count--;
				break;
			}
			i++;
		}
		if(count==0) return true;
		else return false;
	}
	
	//回傳數列中最大值之索引值  (工具)
	public static int[] index_list_of_the_max(int[] array){
		if(array.length==0) return null;
		else{
			int max_value = 0;
			for(int i = 0;i<array.length;i++) if(array[i]>max_value) max_value = array[i];
			if(max_value==0) return null;
			else{
				int count = 0;
				for(int i = 0;i<array.length;i++) if(array[i]==max_value) count++;
				int[] index_list = new int[count];
				count = 0;
				for(int i = 0;i<array.length;i++) if(array[i]==max_value) index_list[count++] = i;
				return index_list;
			}
		}		
	}
	
	public static boolean is_all_true(boolean[] chart){
		boolean all_true = true;
		for(boolean x : chart) if(!x) all_true=false;
		return all_true;
	}
	
	public static void print_buckets(int[][][] buckets){
		System.out.printf("---------------------------\n");
		for(int[][] zzz : buckets) print_buckets(zzz);
		System.out.printf("---------------------------\n");
	}
	
	public static void print_buckets(int[][] buckets){
		System.out.printf("Index| Data\n");
		int index = 0;
		for(int[] zz : buckets)	{
			System.out.printf("%5d| ",index++);
			for(int z : zz) System.out.printf("%d, ",z);
			System.out.printf("\n");
		}
		System.out.printf("\n");
	}
	
	public static void print_buckets(int[] bucket){ 
		System.out.printf("Index| Data\n");
		int index = 0;
		for(int z : bucket) {
			System.out.printf("%5d| ",index++);
			System.out.printf("%d\n",z); 
		}
		System.out.printf("\n");
	}
	
	public static void print_buckets(boolean[] bucket){
		for(boolean z : bucket) System.out.printf("%d, ",z?1:0);
		System.out.printf("\n\n");
	}
	
	public static int[][] show_bucket_union_chart(int[][] buckets){
		int[][] chart= new int[buckets.length][];
		System.out.println("    0  1  2  3  4  5  6  7  8");
		for(int y=0;y<buckets.length;y++){
			chart[y] = new int[y+1];
			System.out.printf("%2d:",y);
			for(int x=0;x<=y;x++){				
				chart[y][x]=bucket_union_counter(buckets[y],buckets[x]);
				System.out.printf("%2d ",chart[y][x]);
			}
			System.out.println("");
		}
		return chart;
	}
	
}
