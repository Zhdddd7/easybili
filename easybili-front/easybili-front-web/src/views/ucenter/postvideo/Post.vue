<template>
  <div class="upload-video-panel">
    <VideoUploader ref="videoUploaderRef"> </VideoUploader>
    <div v-if="startUpload" class="video-form">
      <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="70px"
        @submit.prevent
      >
        <el-form-item label="Cover" prop="videoCover">
          <ImageCoverSelect
            :coverWidth="200"
            :cutWidth="680"
            :scale="0.6"
            :coverImage="formData.videoCover"
          >
          </ImageCoverSelect>
        </el-form-item>
        <!--input输入-->
        <el-form-item label="Title" prop="videoName">
          <el-input
            clearable
            placeholder="Please enter titls"
            v-model="formData.videoName"
            maxlength="100"
            show-word-limit
          ></el-input>
        </el-form-item>
        <!-- 单选 -->
        <el-form-item label="Type" prop="postType">
          <el-radio-group v-model="formData.postType">
            <el-radio :value="0">Original</el-radio>
            <el-radio :value="1">Reprint</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="" prop="originInfo" v-if="formData.postType == 1">
          <el-input
            clearable
            placeholder="Reprint videos should mark the origins(e.g. orginal from https://www.xxxx.com/yyyy)"
            v-model="formData.originInfo"
            maxlength="200"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="Tags" prop="tags">
          <TagInput v-model="formData.tags"></TagInput>
        </el-form-item>
        <el-form-item label="Category" prop="categoryArray">
          <el-cascader
            v-model="formData.categoryArray"
            :options="categoryStore.categoryList"
            :props="{ value: 'categoryId', label: 'categoryName' }"
          />
        </el-form-item>

        <!--textarea输入-->
        <el-form-item label="Intro" prop="introduction">
          <el-input
            clearable
            placeholder="Give us more introduction to your videos:) 	"
            type="textarea"
            :rows="5"
            :maxlength="2000"
            resize="none"
            show-word-limit
            v-model="formData.introduction"
          ></el-input>
        </el-form-item>
        <el-form-item label="Settings" prop="introduction">
          <el-checkbox-group v-model="formData.interactionArray">
            <el-checkbox value="0">Danmu closed</el-checkbox>
            <el-checkbox value="1">Comment closed</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" @click="submitForm">publish</el-button>
          <el-button @click="router.push('/ucenter/video')">cancel</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { useCategoryStore } from "@/stores/categoryStore.js";
const categoryStore = useCategoryStore();
import TagInput from "./TagInput.vue";
import VideoUploader from "./VideoUploader.vue";
import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  onUnmounted,
  watch,
  provide,
  inject,
} from "vue";

import { uploadImage } from "@/utils/Api.js";

const { proxy } = getCurrentInstance();
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

import { mitter } from "@/eventbus/eventBus.js";

const startUpload = ref(false);
mitter.on("startUpload", (fileName) => {
  startUpload.value = true;
  nextTick(() => {
    formDataRef.value.resetFields();
    formData.value = {};
    formData.value.tags = [];
    formData.value.videoName = fileName;
  });
});

const formData = ref({
  tags: [],
});
const formDataRef = ref();
const rules = {
  videoCover: [{ required: true, message: "Cover can not be empty" }],
  videoName: [{ required: true, message: "Title can not be empty" }],
  postType: [{ required: true, message: "Type can not be empty" }],
  originInfo: [{ required: true, message: "Origin info can not be empty" }],
  categoryArray: [{ required: true, message: "Category can not be empty" }],
  tags: [{ required: true, message: "tags can not be empty" }],
};

provide("cutImageCallback", ({ coverImage }) => {
  formData.value.videoCover = coverImage;
});

const videoUploaderRef = ref();
const videoList = ref([]);

const submitForm = () => {
  const uploadFileList = videoUploaderRef.value.getUploadFileList();
  if (!uploadFileList) {
    return;
  }
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {
      uploadFileList: JSON.stringify(uploadFileList),
    };
    Object.assign(params, formData.value);
    //处理分裂
    params.pCategoryId = params.categoryArray[0];
    if (params.categoryArray.length > 1) {
      params.categoryId = params.categoryArray[1];
    }
    delete params.categoryArray;

    //互动设置
    if (params.interactionArray) {
      params.interaction = params.interactionArray.join(",");
      delete params.interactionArray;
    }
    //判断文件
    if (params.videoCover instanceof File) {
      const videoCover = await uploadImage(params.videoCover);
      if (!videoCover) {
        return;
      }
      params.videoCover = videoCover;
    }
    let result = await proxy.Request({
      url: proxy.Api.postVideo,
      showLoading: true,
      params,
    });
    if (!result) {
      return;
    }
    proxy.Message.success("Published!");
    router.push("/ucenter/video");
  });
};

//编辑
const videoId = ref();
const init = async () => {
  nextTick(() => {
    videoUploaderRef.value.initUploader(startUpload.value, []);
  });
  if (videoId.value) {
    let result = await proxy.Request({
      url: proxy.Api.getVideoByVideoId,
      params: {
        videoId: videoId.value,
      },
    });
    if (!result) {
      return;
    }
    formData.value = result.data.videoInfo;
    //处理tags
    formData.value.tags = formData.value.tags.split(",");
    //处理分类
    formData.value.categoryArray = [];
    if (formData.value.pCategoryId) {
      formData.value.categoryArray.push(formData.value.pCategoryId);
    }
    if (formData.value.categoryId) {
      formData.value.categoryArray.push(formData.value.pCategoryId);
    }
    //处理互动设置
    formData.value.interactionArray = formData.value.interaction
      ? formData.value.interaction.split(",")
      : [];

    nextTick(() => {
      videoUploaderRef.value.initUploader(
        startUpload.value,
        result.data.videoInfoFileList
      );
    });
  }
};
watch(
  () => route.query.videoId,
  (newVal, oldVal) => {
    if (newVal) {
      startUpload.value = true;
    } else {
      startUpload.value = false;
    }
    videoId.value = newVal;
    init();
  },
  { immediate: true, deep: true }
);

//重新加载
const reload = () => {};
onMounted(() => {
  window.addEventListener("beforeunload", reload);
});

onUnmounted(() => {
  mitter.off("startUpload");

  window.removeEventListener("beforeunload", reload);
});
</script>

<style lang="scss" scoped>
.upload-video-panel {
  background: #fff;
  padding: 20px;
}
.video-form {
  padding-right: 200px;
}
</style>
