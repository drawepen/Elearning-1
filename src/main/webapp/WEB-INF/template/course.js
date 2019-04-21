let courseTemplate = `
<el-card :body-style="{ padding: '0px' }" style="width: 256px;float: left;margin:10px 10px">
                        <a @click="getCourseDetails(course.cid)" style="text-decoration: none;color: #409EFF;">
                            <img :src="course.courseIcon" class="image">
                        </a>
                            <div style="padding: 14px;">
                                <span style="color: #000;">{{course.courseName}}</span>
                                <template v-if="schedule">
                                    <div class="bottom clearfix">
                                        <time class="time">{{course.courseDesc}}</time>
                                        <el-button type="text" class="button" @click="getCourseDetails(course.cid)">learn</el-button>
                                    </div>
                                </template>
                                <template v-else>
                                    <div style="margin-top: 10px;">
                                        <el-progress :stroke-width="12" :percentage="20/40*100" :show-text="false"></el-progress>
                                        <span>已学习20/40小节</span>
                                    </div>
                                </template>
                            </div>
                    </el-card>
`

var courseModule = {
    data:function () {
        return{

        }
    },
    props:['course','schedule'],
    template: courseTemplate,
    methods:{
        getCourseDetails(cid){
            console.log(cid)
            this.$router.push('/course/details/'+cid);
        }
    },

}

Vue.component("my_course",courseModule);