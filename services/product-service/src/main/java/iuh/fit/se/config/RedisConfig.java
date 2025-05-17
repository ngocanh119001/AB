package iuh.fit.se.config;

import org.redisson.api.RBucketReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig implements CommandLineRunner{

	private final RedissonReactiveClient client;
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		RBucketReactive<String> bucket = client.getBucket("test");
		bucket.set("no value").subscribe();
		
	}
}
