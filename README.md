# cs426-mobile-development-ecomerce

## Qui chuẩn tạo branch:
- Tạo nhánh tên: features/tên-task. VD: features/list-product. 
- B1: checkout nhánh sang main
- B2: git checkout -b "features/..."
- B3: làm việc trên nhánh mình, git push origin 'nhánh mình' thoải mái
- B4: sau khi làm xong thì tạo merge request SANG MAIN, và chờ một người đứng ra merge vào main
### Notes: 
- Khi nhận task mới thì nhớ checkout sang main trước và git fetch, git pull của main (để cập nhật code mới nhất) và tạo branch mới từ đây
## Cấu trúc folder:
```
mobile-development-ecomerce
│   README.md
│       
└───app
    └───src
        └───main
            │   java
                │   findandbuy
                │   activity
                │   fragment
                │   map
                │   adapter
                │   songAdapter
                │   song
                │   listView
                │   CustomizeView
                │   Sprint/Animation
                │   HelperFunction
                │   Model
            │   res
            │   assets
```
