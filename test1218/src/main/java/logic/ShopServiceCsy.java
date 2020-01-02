package logic;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
@Service
public class ShopServiceCsy implements ShopService {
	@Autowired
	private ItemDao itemDao;

	@Override
	public List<Item> getItemList() {
		return itemDao.list();
	}

	@Override
	public void itemCreate(Item item, HttpServletRequest request) {
		// 업로드 된 이미지 파일 존재 request업로드 경로 쓰려고
		if (item.getPicture() != null && !item.getPicture().isEmpty()) {
			// 파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(), request, "item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			// 사진 url중 순수 사진 이름만 가져와서 item에 저장
		}
		itemDao.insert(item);

	}

	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		// picture : 업로드된 파일의 내용
		String orgFile = picture.getOriginalFilename();
		String uploadPath = request.getServletContext().getRealPath("/") + path;
		File fpath = new File(uploadPath);
		if (!fpath.exists()) {
			fpath.mkdirs();
		}
		try {
			// picture 업로드된 파일의 내용을 파일로 생성
			picture.transferTo(new File(uploadPath + orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Item getItem(String id) {
		return itemDao.selectOne(id);
	}

	@Override
	public void itemUpdate(Item item, HttpServletRequest request) {
		// 수정된 이미지 존재
		if (item.getPicture() != null && !item.getPicture().isEmpty()) {
			// 파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(), request, "item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
			// 사진 url중 순수 사진 이름만 가져와서 item에 저장
		}
		itemDao.update(item);

	}

	@Override
	public void itemDelete(String id) {
		itemDao.delete(id);

	}

}
