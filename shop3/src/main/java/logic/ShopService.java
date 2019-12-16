package logic;

import java.util.List;
import java.io.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;

@Service // @Component + service 기능 
public class ShopService {
	@Autowired
	private ItemDao itemDao;

	public List<Item> getItemList() { // 가져온 정보를 리스트로 만들어서 리턴
		return itemDao.list();
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		// 업로드 된 이미지 파일 존재                    request업로드 경로 쓰려고 
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			// 파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(), request, "item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			//사진 url중 순수 사진 이름만 가져와서 item에 저장
		}
		itemDao.insert(item);
		
	}

	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		// picture : 업로드된 파일의 내용
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/")+path;
		File fpath = new File(uploadPath);
		if(!fpath.exists()) {
			fpath.mkdirs();
		}
		try {
			// picture 업로드된 파일의 내용을 파일로 생성 
			picture.transferTo(new File(uploadPath+orgFile));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
