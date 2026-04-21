let currentTab = "need";

let needPosts = [];
let offerPosts = [];

let needTabBtn = document.getElementById("needTab");
let offerTabBtn = document.getElementById("offerTab");
let feedHeading = document.getElementById("feedHeading");

needTabBtn.addEventListener("click", function () {
  currentTab = "need";
  switchTab();
});

offerTabBtn.addEventListener("click", function () {
  currentTab = "offer";
  switchTab();
});

function switchTab() {
  needTabBtn.classList.toggle("active-tab", currentTab === "need");
  offerTabBtn.classList.toggle("active-tab", currentTab === "offer");

  feedHeading.innerText =
    currentTab === "need" ? "I NEED POSTS" : "I OFFER POSTS";

    renderPosts();
}

let postBtn = document.getElementById("postBtn");

postBtn.addEventListener("click", function () {
  let title = document.getElementById("postTitle").value;
  let details = document.getElementById("postDetails").value;
  let price = document.getElementById("postPrice").value;

  if (title === "" || price === "") {
    alert("Fill title and price!");
    return;
  }

  let hostel = document.getElementById("hostelSelect").value;

  let post = {
    title: title,
    details: details,
    price: price,
    interestedUsers: [],
    showInput: false,
    selectedUser: null,
    hostel: hostel
  };

  if (currentTab === "need") {
    needPosts.push(post);
  } else {
    offerPosts.push(post);
  }

  renderPosts();

  clearForm();

});



let postFeed = document.getElementById("postFeed");

function renderPosts() {
  postFeed.innerHTML = "";

  let selectedHostel = document.getElementById("hostelSelect").value;

  let posts = (currentTab === "need" ? needPosts : offerPosts)
    .filter(function(post) {
      return selectedHostel === "" || post.hostel === selectedHostel;
    });
  let hostelSelect = document.getElementById("hostelSelect");

  hostelSelect.addEventListener("change", function () {
    renderPosts();
  });
  posts.forEach(function (post, index) {
    let postDiv = document.createElement("div");
    postDiv.className = "post-card";

    postDiv.innerHTML = `
      <div class="post-title">${post.title}</div>
      <div>${post.details}</div>
      <div class="post-price">₹${post.price}</div>

      ${
        currentTab === "need"
          ? post.showInput
            ? `
              <div>
                <input id="nameInput-${index}" placeholder="Enter your name" />
                <button onclick="submitInterest(${index})">Submit</button>
              </div>
            `
            : `<button onclick="showInputBox(${index})">Offer Help</button>`
          : ""
      }

      <div style="margin-top:8px;">
        <div style="font-size:12px; color:gray;">
          ${(post.interestedUsers || []).length} interested
        </div>

        ${
          post.selectedUser
            ? `<div style="color:green; font-weight:bold;">✅ Selected: ${post.selectedUser}</div>`
            : (post.interestedUsers || []).length > 0
              ? post.interestedUsers.map((user, i) => `
                  <div>
                    👤 ${user}
                    ${
                      currentTab === "need"
                        ? `<button onclick="acceptUser(${index}, ${i})" style="margin-left:10px;">Accept</button>`
                        : ""
                    }
                  </div>
                `).join("")
              : "<div style='font-size:12px;color:gray;'>No one yet</div>"
        }
      </div>
    `;

    postFeed.appendChild(postDiv);
  });
}


function clearForm() {
  document.getElementById("postTitle").value = "";
  document.getElementById("postDetails").value = "";
  document.getElementById("postPrice").value = "";
}

//interests
function markInterested(index) {
  let name = prompt("Enter your name");
  if (!name) return;
  name = name.trim();
  if(name === ""){
    alert("Name cannot be empty");
    return;
  }

  let posts = currentTab === "need" ? needPosts : offerPosts;

  // safety check
  if (!posts[index].interestedUsers) {
    posts[index].interestedUsers = [];
  }

  //prevent doplicate
  if (posts[index].interestedUsers.includes(name)) {
    alert("You have already shown interest!");
    return;
  }

  posts[index].interestedUsers.push(name);

  renderPosts();
}

function showInputBox(index) {
  let posts = currentTab === "need" ? needPosts : offerPosts;

  posts[index].showInput = true;

  renderPosts();
}

function submitInterest(index) {
  let input = document.getElementById(`nameInput-${index}`);
  let name = input.value;

  if (!name) return;

  name = name.trim();

  if (name === "") {
    alert("Name cannot be empty");
    return;
  }

  let posts = currentTab === "need" ? needPosts : offerPosts;

  if (!posts[index].interestedUsers) {
    posts[index].interestedUsers = [];
  }

  if (posts[index].interestedUsers.includes(name)) {
    alert("You have already shown interest!");
    return;
  }

  posts[index].interestedUsers.push(name);


  posts[index].showInput = false;

  renderPosts();
}

//accpt fn

function acceptUser(postIndex, userIndex) {
  let posts = currentTab === "need" ? needPosts : offerPosts;

  let selected = posts[postIndex].interestedUsers[userIndex];

  posts[postIndex].selectedUser = selected;

  renderPosts();
}