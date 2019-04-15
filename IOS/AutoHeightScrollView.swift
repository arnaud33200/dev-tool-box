class AutoHeightScrollView: UIScrollView {
    
    let viewContainer = UIView(frame: CGRect.zero)
    var totalHeight: CGFloat = 0.0 // calculated for every new view
    
    var bottom: CGFloat = 20.0
    var sidePadding: CGFloat = 20.0
    var maxWidth: CGFloat = 500.0
    
    class ScrollItem {
        var top: CGFloat = 20.0
        var height: CGFloat = 0.0
        var view: UIView!
        
        init(_ view: UIView, top: CGFloat, height: CGFloat) {
            self.view = view
            self.top = top
            self.height = height
        }
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        initView()
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        initView()
    }
    
    override func layoutSubviews() {
        var width = frame.width  - CGFloat((sidePadding * 2))
        if (width > maxWidth) {
            width = maxWidth
        }
        viewContainer.widthAnchor.constraint(equalToConstant: width).isActive = true
        
        let height = totalHeight + bottom
        viewContainer.heightAnchor.constraint(equalToConstant: height).isActive = true
        contentSize = CGSize(width: viewContainer.frame.width, height: height)
    }

    private func initView() {
        backgroundColor = UIColor.clear

        viewContainer.backgroundColor = UIColor.clear
        addSubview(viewContainer)
        
        viewContainer.translatesAutoresizingMaskIntoConstraints = false
        viewContainer.topAnchor.constraint(equalTo: topAnchor).isActive = true
        viewContainer.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
        
        // DOESN'T WORK FOR SOME REASON
//        viewContainer.leftAnchor.constraint(equalTo: leftAnchor, constant: 20.0).isActive = true
//        viewContainer.rightAnchor.constraint(equalTo: rightAnchor, constant: 20.0).isActive = true
    }
    
    func testAddFakeData() {
        for index in 0...25 {
            let view = UIView(frame: CGRect.zero)
            view.backgroundColor = index % 2 == 0 ? UIColor.green : UIColor.blue
            addScrollItemView(AutoHeightScrollView.ScrollItem.init(view, top: 20, height: 50))
        }
    }
    
    func addScrollItemView(_ scrollItem: ScrollItem) {
        viewContainer.addSubview(scrollItem.view)
        scrollItem.view.translatesAutoresizingMaskIntoConstraints = false
        scrollItem.view.heightAnchor.constraint(equalToConstant: scrollItem.height).isActive = true
        scrollItem.view.leftAnchor.constraint(equalTo: viewContainer.leftAnchor).isActive = true
        scrollItem.view.rightAnchor.constraint(equalTo: viewContainer.rightAnchor).isActive = true
        
        let count = viewContainer.subviews.count - 1
        let anchor = count > 0 ? viewContainer.subviews[count - 1].bottomAnchor : viewContainer.topAnchor
        scrollItem.view.topAnchor.constraint(equalTo: anchor, constant: scrollItem.top).isActive = true
        totalHeight = totalHeight + scrollItem.top + scrollItem.height
    }
}
