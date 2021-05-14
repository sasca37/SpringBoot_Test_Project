package com.mainline.magic.scheduler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MagicSchedulerApplicationTests {

//	@Mock
//    private Scheduler scheduler;

	void contextLoads() {

		JobKey jobKey = new JobKey("김동우", "메인라인");
//		System.out.println(scheduler + " ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
//		try { 
//			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//			JobDetail detail = JobUtils.createJob(jobKey, InsuranceJob.class);
//			Trigger trigger = JobUtils.createTrigger(jobKey);
//			scheduler.start();
//			scheduler.scheduleJob(detail,trigger);
//			System.out.println("==========================================");
//			try {
//				Thread.sleep(3 * 1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//		} catch (SchedulerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public void test() {
		System.out.println("aaaaaaaaaaaaaaaaa");
		System.out.println(System.currentTimeMillis());
		File file = new File("C:\\Users\\mainline\\Desktop\\replace_all_word05_resut.docx");
		sendMts(file);
	}

	private void sendMts(File file) {
		String URL = "http://172.26.129.214:8080/MagicScheduler/files";
		OutputStream outputStream;

		String result = "";

		try {
			URL url = new URL(URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Content-Type", "multipart/form-data;charset=utf-8;boundary=aaaaaaaaaaaaaa");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			outputStream = connection.getOutputStream();

			FileInputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
			inputStream.close();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				result = response.toString();
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				result = response.toString();
			}

			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fileUpload() throws IOException {
		System.out.println("aaaaaaaa");
		File file = new File("C:\\Users\\mainline\\Desktop\\replace_all_word05_resut.docx");
//		"http://172.26.129.214:8080/MagicScheduler/files"
		try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final HttpPost httppost = new HttpPost("http://172.26.129.214:8080/MagicScheduler/files");

			final FileBody bin = new FileBody(file);
			final StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

			final HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("files", bin)
					.addPart("comment", comment).build();

			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost);
			try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
				System.out.println("----------------------------------------");
				System.out.println(response);
				final HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			}
		}
	}

	
	public void unzip() {
		File file = new File("C:\\Temp\\test\\F2E54BC2292E42CFA8EC9F66ABF4259E");
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			ZipInputStream inputStream = new ZipInputStream(fileInputStream);
			Path path = Paths.get("C:\\Temp\\test\\download"+File.separator+file.getName());
		
			for (ZipEntry entry; (entry = inputStream.getNextEntry()) != null;) {
				Path resolvedPath = path.resolve(entry.getName());
				if (!entry.isDirectory()) {
					Files.createDirectories(resolvedPath.getParent());
					Files.copy(inputStream, resolvedPath);
				} else {
					Files.createDirectories(resolvedPath);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void listTest() {
		List<String> list  = new ArrayList<String>();
		list.add("aaaa");
		list.add("aaaa");
		list.add("aaaa");
		list.add("aaaa");
		list.add("aaaa");
		list.add("aaaa");
		System.out.println(list.toString());
	}

}
