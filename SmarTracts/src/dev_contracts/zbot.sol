// SPDX-License-Identifier: MIT
pragma solidity >=0.7.0 <0.8.0;

contract Zbot {
    address payable owner;
    mapping(string=>address payable) addresses;
    mapping(string=>uint) balances;
    mapping(address=>string) ids;
    //uint public fee;
    string public version;

    event Register(address addr, string id);
    event Deposit(string id, uint amount);
    event Tip(string from, string to, uint amount);
    event Withdraw(string id);

    constructor() {
        owner=msg.sender;
        version="1.2.0";
    }
    modifier onlyOwner() {
        require(msg.sender==owner, "You are not the owner.");
        _;
    }
    function register(address payable addr, string memory id) public onlyOwner {
        addresses[id]=addr;
        ids[addr]=id;
        emit Register(addr, id);
    }
    function getAddress(string memory id) public view returns(address) {
        return addresses[id];
    }
    function deposit(string memory id) public payable {
        require(addresses[id]!=address(0), "No address registered with account.");
        balances[id]+=msg.value;
        emit Deposit(id, msg.value);
    }
    function balance(string memory id) public view returns(uint) {
        return balances[id];
    }
    function tip(string memory from, string memory to, uint amount) public onlyOwner {
        require(balances[from]>=amount, "Not enough balance.");
        balances[from]-=amount;
        balances[to]+=amount;
        emit Tip(from, to, amount);
    }
    function withdraw(string memory id) public onlyOwner {
        address payable addr=addresses[id];
        require(addr!=address(0), "No address registered with account.");
        addr.transfer(balances[id]);
        balances[id]=0;
        emit Withdraw(id);
    }

    function receive() external payable {
        bytes memory tempEmptyStringTest = bytes(ids[msg.sender]);
        require(tempEmptyStringTest.length != 0, "No user ID found for address.");
        deposit(ids[msg.sender]);
    }

}
