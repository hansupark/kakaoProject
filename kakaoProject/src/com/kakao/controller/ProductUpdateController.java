package com.kakao.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kakao.VO.ProductVO;
import com.kakao.service.ImageService;
import com.kakao.service.ProductService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class ProductUpdateController implements Controller {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProductService service = ProductService.serviceGetInstance();
		ImageService service_2 = ImageService.getInstance();
		req.setCharacterEncoding("UTF-8");
		int maxSize = 3 * 1024 * 1024;
		String encoding = "UTF-8";
		String saveDir = "C:\\dev\\workplace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\kakaoProject\\쇼핑몰\\쇼핑몰\\test";
		MultipartRequest multi = new MultipartRequest(req, saveDir, maxSize, encoding, new DefaultFileRenamePolicy());
		
		ProductVO vo = new ProductVO();
		int num = Integer.parseInt(multi.getParameter("num")); //productNum
		String name = multi.getParameter("name");
		int price = Integer.parseInt(multi.getParameter("price"));
		String detail = multi.getParameter("detail");
		String category = multi.getParameter("category");
		String cha = multi.getParameter("cha");
		vo.setName(name);
		vo.setDetail(detail);
		vo.setPrice(price);
		vo.setCategory(category);
		vo.setCha(cha);
		service.updateProduct(vo,num);
		
		
		String realSaveDir = "쇼핑몰/쇼핑몰/test";
		//ArrayList<String> fileNames = new ArrayList<String>();
		HashMap<String,String> fileNames = new HashMap<String,String>();
		String formname;
		String fileName;
		
		int hasImage[] = new int[6];		
		Enumeration form = multi.getFileNames();
		
		for(int x = 0 ; x < 6 ; x++)
		{
			hasImage[x] = Integer.parseInt(multi.getParameter("file"+(x+1)));
		}
		while(form.hasMoreElements())
		{	
			formname = (String) form.nextElement();
			System.out.println(formname);
			fileName = multi.getFilesystemName(formname);
			System.out.println(fileName);
			if(fileName != null)
			{
				fileNames.put(formname, fileName);
			}
			else
				fileNames.put(formname, "null");			
		}
		System.out.println();
		
		

		
		
		for(int x = 0 ; x < 6 ; x++)
		{
			int image = hasImage[x];
			String file = fileNames.get("image"+(x+1));
			
			if(image !=0  && !(file.equals("null")))
			{
				System.out.println("원래 있던거 수정");
				if(service_2.isMain(image))
				{
					System.out.println("main 수정");
					service_2.deleteImage(image);
					service_2.insertImage(realSaveDir, file,num,"main");
				}
				else
				{
					System.out.println("sub 수정");
					service_2.deleteImage(image);
					service_2.insertImage(realSaveDir, file,num);
				}
			}
			else if(image == 0 && !(file.equals("null")))
			{
				System.out.println("새로 추가");
				service_2.insertImage(realSaveDir, file, num);
			}
			
		}
		HttpUtil.forward(req, res,"ProductAdmin.jsp");
		return;
	}

}
