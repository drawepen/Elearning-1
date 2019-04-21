/*
头部栏组件
 */
var headerTemplate = `
<el-header height="100px">
    <el-row type="flex" class="row-bg" justify="space-around">
        <el-col :span="12" style="display: flex;align-items: center;justify-content: center;">
            <router-link to="/"><img src="./img/logo.jpg" height="50px" width="auto" style="margin-right:20px"></router-link>
            <i>让知识回归平凡</i>
        </el-col>
        <el-col :span="6"><el-input v-model="searchKey" placeholder="搜索..." @keyup.enter.native="handleSearch"></el-input></el-col>
        <el-col :span="4">
            <el-button type="text" @click="toLoginRegiste('/login')">登录</el-button>
            <el-button type="text" @click="toLoginRegiste('/registe')">注册</el-button>
        </el-col>
    </el-row>
    <el-row class="row-md">
        <el-col :span="20" :offset="3">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{path:'/'}">首页</el-breadcrumb-item>
                <el-breadcrumb-item :to="{path:'/fileMain'}">文件资源</el-breadcrumb-item>
                <el-breadcrumb-item :to="{path:'/exercise'}">练习测试</el-breadcrumb-item>
                <el-breadcrumb-item :to="{path:'/activity'}">活动详情</el-breadcrumb-item>
            </el-breadcrumb>
        </el-col>
    </el-row>
    </el-header>
`

var headerModule = {
    data:function () {
        return{
            searchKey:''
        }
    },
    props:[],
    template: headerTemplate,
    created(){
        this.keyupEnter()
    },
    methods:{
        keyupEnter(){
            document.onkeydown = e =>{
                let body = document.getElementsByTagName('body')[0]
                if (e.keyCode === 13 && e.target.baseURI.match(/inputbook/) && e.target === body) {
                    console.log('enter')
                    this.handleSearch()
                }
            }
        },
        handleSearch:function(){
           console.log(this.searchKey)
        },
        toLoginRegiste:function(to){
            root.$router.push(to);
        }
    },

}

Vue.component("my_header",headerModule);