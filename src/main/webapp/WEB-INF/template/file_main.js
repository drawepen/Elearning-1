/*
文件模块的主页面组件
 */
var fileMainTemplate = `
<el-main style="margin-top: 20px">
        <el-row style="display: flex;flex-direction: column;align-items: center">
            <el-col :span="16" style="margin-bottom: 20px">
                <el-card shadow="always" style="padding: 20px;">
                    <h1 style="margin-bottom: 10px;font-size: 48px">Hello , world</h1>
                    <p>每一个不曾起舞的日子都是对生命的浪费</p>
                    <el-button type="primary" @click="gotoUpload">上传<i class="el-icon-upload el-icon--right"></i></el-button>
                </el-card>
            </el-col>

            <el-col :span="16">
                <el-cascader :options="colleges" :props="college_major" filterable :change-on-select="false"></el-cascader>
            </el-col>

            <el-col :span="16" style="margin-top: 20px">
                <my_file v-for="jfile in my_files" :jfile="jfile" :file_tags="file_tags"></my_file>
            </el-col>
        </el-row>
    </el-main>
`

var fileMainModule = {
    data:function () {
        return{
            my_files:[],
            colleges: [
                {
                    id:1,
                    name:'软件学院',
                    _majors:[
                        {
                            id:2,
                            name:"软件工程"
                        },
                        {
                            name:"数媒"
                        }
                    ]
                }
            ],
            college_major:{
                value: 'id',
                label: 'name',
                children: '_majors'
            },
            file_tags:[
                {
                    tagName:"数据结构"
                },
                {
                    tagName:"操作系统"
                }
            ]
        }
    },
    props:[],
    template: fileMainTemplate,
    created:function(){
        var _this =this;
        axios.get('file/getFileListByCourse',{
            params:{
                cid:100
            }
        })
            .then(function(response){
                _this.my_files = response.data.data.files;
            })
            .catch(function(err){
                console.log(err);
            });
    },
    methods:{
        gotoUpload:function(){
            this.$router.push('/fileUpload')
        }
    },

}

Vue.component("my_file_main",fileMainModule);