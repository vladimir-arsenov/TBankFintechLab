# Инструкции по запуску
## Через консоль
1. Скачайте    ![image](https://github.com/user-attachments/assets/dd706255-5e5a-4948-915e-f1b9446df6ad) файл.
2. Откройте консоль и перейдите коммандой cd в директорию, в которую установлен .jar файл.
3. Напишите команду java -jar TBankFintechLab.jar и приложение запуститься.
 ![image](https://github.com/user-attachments/assets/e2d86ba1-3c42-4cb5-a2de-1dc6b07c4e38)

## Через IntelliJ
1. Скопируйте ссылку репозитория  
   ![image](https://github.com/user-attachments/assets/c97062a2-12fa-4e65-a03a-08780139bd15)
2. Создайте новый проект в IntelliJ используя ссылку.
3. Выберете файл ![image](https://github.com/user-attachments/assets/e7c018f4-e72f-4b6d-92c8-9e651563ebc1) и запустите его ![image](https://github.com/user-attachments/assets/ca35ef7b-3b0c-4341-8fbc-64b8ad483f89)

# Инструкции по использованию
В Postman отправьте запрос указав в теле искодный язык, язык перевода и текст:  
POST http://localhost:8080/translate  
{  
  "text": "Hello",  
  "sourceLang": "en",  
  "targetLang": "ru"  
}    
![image](https://github.com/user-attachments/assets/0d4eceed-9422-41b7-997d-c140772c70f9)
