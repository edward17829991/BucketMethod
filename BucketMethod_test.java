public class BucketMethod_test {

	public static void main(String[] args) {		
		int[][] buckets= {
				{1,2,5,7},
				{2,3,5,7},
				{2,4,8},
				{2,3,8},
				{2,7},
				{1,3},
				{2},
				{1,5,6},
				{1,5,7}	};
		
		
		BucketMethod Bucket = new BucketMethod();		
		
		System.out.printf("direct mapping:\n");
		BucketMethod.print_buckets(buckets);
		
		System.out.printf("new mapping:\n");
		int[][] new_mapping = Bucket.new_mapping(buckets);
		BucketMethod.print_buckets(new_mapping);
		BucketMethod.print_buckets(BucketMethod.new_index(buckets,new_mapping));
	}
}
