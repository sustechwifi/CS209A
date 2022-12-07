// 跨域配置

let ip =  'localhost'

module.exports = {
  devServer: {
    port: 8081,
    proxy: {
      '/api': {
        target: 'http://'+ip+':9090',     //代理的目标地址
        changeOrigin: true,              //是否设置同源，输入是的
        pathRewrite: {                   //路径重写
          '^/api': ''                     //选择忽略拦截器里面的内容
        }
      }
    }
  }
}
