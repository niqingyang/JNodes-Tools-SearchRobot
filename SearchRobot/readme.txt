
作者：niqingyang
邮箱：niqy@qq.com
讨论QQ群：107249241

1.SearchRobot是一款用Java开发的文件内容检索工具，开发目的是为了用于快速查找代码、分析代码、阅读代码用的。
2.SearchRobot程序的同目录下会存在一个search.data的文件来保存搜索的相关配置和历史记录，用户可以自行修改。
3.SearchRobot搜索结果用表格展示出来，右键弹出菜单“打开文件”、“打开目录”两个选项，“打开文件”默认采用Notepad++进行打开，
   如果您安装了此程序，并且安装目录为默认安装目录则自动采用此程序打开，如果没有安装则会用txt程序打开，安装目录不是默认安
   装目录的话请自行修改search.data配置文件的file.open.program属性。
4.SearchRobot集成了Jad，结果就是对于检索.jar文件时，程序可以对.class文件进行反编译再进行搜索，Jad.exe请放在C盘根目录或者
  SearchRobot同目录下，或自行修改search.data配置文件中的jad.cmd属性。
5.SearchRobot目前只提供了英文和中文两种语言的支持。

