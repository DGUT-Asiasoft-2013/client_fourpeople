package com.example.fourpeople.campushousekeeper.chat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ScenceHandle {
	private List<Scence> scenceList;
	List<String> conversation=new ArrayList<String>();
	Scence scence = new Scence();

	public ScenceHandle() {
		scenceList=new ArrayList<Scence>();
		
		
	}

	public void createScence() {
		for (int i = 0; i < 10; i++) {
			
			scence.setIndex(i);
			scence.setMscene("����" + i);
			scenceList.add(i, scence);
			scence = new Scence();

		}
	}

	public Scence getScene(int index) {

		return scenceList.get(index);
	}

	public class Scence {
		private int index;
		private String mscene;

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getMscene() {
			return mscene;
		}

		public void setMscene(String mscene) {
			this.mscene = mscene;
		}

	}
	
	
	public void analyseData(String filepath,int index){

        String path = filepath;
             //���ļ�
             File file = new File(path);
             //���path�Ǵ��ݹ����Ĳ�����������һ����Ŀ¼���ж�
             if (file.isDirectory())
             {
                 Log.d("TT", "The File doesn't not exist.");
             }
             else
             {
                 try {
                     InputStream instream = new FileInputStream(file); 
                    if (instream != null) 
                    {
                         InputStreamReader inputreader = new InputStreamReader(instream);
                         BufferedReader buffreader = new BufferedReader(inputreader);
                         String line;
                         //���ж�ȡ
                         int i=0;
                         while (( line = buffreader.readLine()) != null) {
                            conversation.add(i++, line);
                            
                         }                
                        instream.close();
                     }
                 }
                 catch (java.io.FileNotFoundException e) 
                {
                     Log.d("TT", "The File doesn't not exist.");
                 } 
                catch (IOException e) 
                {
                      Log.d("TT", e.getMessage());
                 }
             }
            
	}
}
