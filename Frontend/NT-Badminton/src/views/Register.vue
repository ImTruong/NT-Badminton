<script setup>

</script>

<template>
  <div class="container">
    <Header class="header"></Header>
    <div class="content">
      <div class="title">
        <h1>Đăng ký</h1>
      </div>
      <div class="form-container">
        <form class="form-register" @submit.prevent>
          <input type="text" name="firstName" id="first-name" placeholder="Họ" required>
          <input type="text" name="lastName" id="last-name" placeholder="Tên" required>
          <input type="email" name="email" id="email" placeholder="Email" required>
          <input type="password" name="password" id="password" placeholder="Mật khẩu" required>
          <input type="number" name="phone" id="phone" placeholder="Số điện thoại" required>
          <div class="birthday input-div">
            <span>
              Ngày sinh:
            </span>
            <input type="date" name="birthday" id="date">
          </div>
          <div class="gender-choice input-div">
            <span>
              Giới tính:
            </span>
            <div class="gender-wrap">
              <label for="gender-male">Nam</label>
              <input type="radio" name="gender" id="gender-male" value="Nam" required>
            </div>
            <div class="gender-wrap">
              <label for="gender-female">Nữ</label>
              <input type="radio" name="gender" id="gender-female" value="Nữ" required>
            </div>
          </div>
        </form>
      </div>
      <div class="submit-container">
        <button type="submit" form="form-register" class="btn-submit">Đăng ký</button>
        <span>
          Bạn đã có tài khoản? <router-link to="/login">Đăng nhập</router-link>
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
  input[type="number"] {
    -moz-appearance: textfield; /* Firefox */
  }
  input[type="number"]::-webkit-outer-spin-button,
  input[type="number"]::-webkit-inner-spin-button {
    -webkit-appearance: none; /* Chrome, Safari, Edge */
    margin: 0;
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
  #date{
    width: 71%;
    padding: 5px
  }
  .birthday{
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>