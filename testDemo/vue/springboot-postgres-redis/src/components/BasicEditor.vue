<template>
  <div>
    <div style="border: 1px solid #ccc; margin-top: 10px">
      <Toolbar
          :editor="editorRef"
          :defaultConfig="toolbarConfig"
          :mode="mode"
          style=" border-bottom: 1px solid #ccc"
      />
      <Editor
          id="editor"
          :defaultConfig="editorConfig"
          :mode="mode"
          v-model="valueHtml"
          style="overflow-y: hidden;height:300px;"
          @onCreated="handleCreated"
          @onChange="handleChange"
          @onDestroyed="handleDestroyed"
          @onFocus="handleFocus"
          @onBlur="handleBlur"
          @customAlert="customAlert"
          @customPaste="customPaste"
      />
    </div>
    <div style="margin-top: 10px">
      <textarea
          id="content"
          v-model="valueHtml"
          readonly
          style="width: 100%;outline: none"
      ></textarea>
    </div>
  </div>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css';
import { onBeforeUnmount, ref, shallowRef, onMounted } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import { IEditorConfig } from '@wangeditor/editor'

let uploadURL = "http://"+window.server.filesUploadUrl+":9090/files/uploadEditorFile";

export default {
  components: { Editor, Toolbar },
  setup() {
    // 编辑器实例，必须用 shallowRef，重要！
    const editorRef = shallowRef();

    // 内容 HTML
    const valueHtml = ref('<p>在此编辑内容</p>');
    const toolbarConfig = {};
    const editorConfig = { MENU_CONF: {} }

    editorConfig.MENU_CONF['uploadImage'] = {

      server: "http://"+window.server.filesUploadUrl+":9090/files/uploadEditorFile", // 上传图片地址

      timeout: 5 * 1000, // 5s

      fieldName: 'file',

      maxFileSize: 10 * 1024 * 1024, // 10M

      base64LimitSize: 5 * 1024, // 5kb 以下插入 base64

      onBeforeUpload(files) {
        console.log('onBeforeUpload', files)

        return files // 返回哪些文件可以上传
        // return false 会阻止上传
      },
      onProgress(progress) {
        console.log('onProgress', progress)
      },
      onSuccess(file, res) {
        console.log('onSuccess', file, res)
      },
      onFailed(file, res) {
        alert(res.message)
        console.log('onFailed', file, res)
      },
      onError(file, err, res) {
        alert(err.message)
        console.error('onError', file, err, res)
      },

    }

    editorConfig.MENU_CONF['uploadVideo'] = {
      server: "http://"+window.server.filesUploadUrl+":9090/files/uploadEditorFile",

      // form-data fieldName ，默认值 'wangeditor-uploaded-video'
      fieldName: 'file',

      maxFileSize: 20 * 1024 * 1024, // 20M

      // 最多可上传几个文件，默认为 5
      maxNumberOfFiles: 1,

      // 超时时间，默认为 30 秒
      timeout: 15 * 1000, // 15 秒

      // 视频不支持 base64 格式插入
    }

    // 组件销毁时，也及时销毁编辑器，重要！
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    // 编辑器回调函数
    const handleCreated = (editor) => {
      console.log('created', editor);
      editorRef.value = editor; // 记录 editor 实例，重要！
    };
    const handleChange = (editor) => {

    };
    const handleDestroyed = (editor) => {
      console.log('destroyed', editor);
    };
    const handleFocus = (editor) => {
      console.log('focus', editor);
    };
    const handleBlur = (editor) => {
      console.log('blur', editor);
    };
    const customAlert = (info, type) => {
      alert(`【自定义提示】${type} - ${info}`);
    };
    const customPaste = (editor, event, callback) => {
      console.log('ClipboardEvent 粘贴事件对象', event);

      // 自定义插入内容
      editor.insertText('');

      // 返回值（注意，vue 事件的返回值，不能用 return）
      callback(true); // 返回 false ，阻止默认粘贴行为
      // callback(true) // 返回 true ，继续默认的粘贴行为
    };

    const insertText = () => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.insertText('hello world');
    };

    const printHtml = () => {
      const editor = editorRef.value;
      if (editor == null) return;
      console.log(editor.getHtml());
    };

    const disable = () => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.disable()
    };

    return {
      editorRef,
      mode: 'default',
      valueHtml,
      toolbarConfig,
      editorConfig,
      handleCreated,
      handleChange,
      handleDestroyed,
      handleFocus,
      handleBlur,
      customAlert,
      customPaste,
      insertText,
      printHtml,
      disable
    };
  },
};
</script>
