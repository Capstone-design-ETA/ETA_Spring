package ETA.whats_your_eta.api.domain.image.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // S3 이용 위한 AmazonS3Client 객체 생성 메소드
    @PostConstruct // 객체 생성 후에 호출되어 AmazonS3Client 초기화함
    public AmazonS3Client amazonS3Client() {
        // AWS 접근 위한 기본 인증 정보 생성
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public List<String> upload(List<MultipartFile> multipartFiles) {
        List<String> imgUrlList = new ArrayList<>();

        // forEach 구문을 통해 multipartFiles로 넘어 온 파일들을 하나씩 fileNameList에 추가
        for (MultipartFile file : multipartFiles) {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(new PutObjectRequest(bucket + "/post/image", fileName, inputStream, objectMetadata));
                imgUrlList.add(s3Client.getUrl(bucket+"/post/image", fileName).toString());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패했습니다.");
            }
        }
        return imgUrlList;
    }

    // 이미지 파일명 중복 방지
    private String createFileName(String originalFilename) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
    }

    // 파일 유효성 검사
    private String getFileExtension(String originalFilename) {

        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다.");
        }
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}
