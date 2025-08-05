<script setup>

</script>

<template>
  <div class="container">
    <Header class="header"></Header>
    <div class="content">
      <div class="title">
        <h1>Đăng nhập</h1>
      </div>
      <div class="form-container">
        <form class="form-register" @submit.prevent>
          <input type="email" name="email" id="email" placeholder="Email" required>
          <input type="password" name="password" id="password" placeholder="Mật khẩu" required>
        </form>
      </div>
      <div class="submit-container">
        <button type="submit" form="form-register" class="btn-submit">Đăng nhập</button>
        <span>
          Bạn chưa có tài khoản? <router-link to="/register">Đăng ký</router-link>
        </span>
      </div>
    </div>
  </div>


</template>

<style scoped>
.container{
  width: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.header{
  margin-bottom: 20px;
}
.content{
  display: flex;
  width: 450px;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
.title{
  width: 100%;
  text-align: center;
  color: var(--main-color);
  padding-top: 15px;
}
.title h1{
  font-weight: bold;
  font-size: 26px;
}
.form-container{
  width: 100%;
}
.form-register{
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding: 20px;
}
.form-register > input{
  width: 90%;
  padding: 10px;
}

.input-div{
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 15px;
}
.gender-wrap{
  height: 100%;
  display: flex;
  align-items: center;
  gap: 5px;
}
.submit-container{
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;
  padding: 0 20px 20px 20px;
}
.btn-submit{
  width: 92%;
  padding: 10px;
  background-color: var(--main-color);
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  &:hover{
    background-color: #ffffff;
    color: var(--main-color);
    border: 1px solid var(--main-color);
    transition: background-color 0.3s ease, color 0.3s ease, border 0.3s ease;
  }
}
.submit-container span{
  margin: 20px 20px 0 0;
  align-self: flex-end;
}
.birthday{
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>